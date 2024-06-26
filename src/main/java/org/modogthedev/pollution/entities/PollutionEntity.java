package org.modogthedev.pollution.entities;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.pollution.system.worldPollution.ClientWorldPollutionData;
import org.modogthedev.pollution.system.worldPollution.ModWorldPollution;
import org.modogthedev.pollution.registry.ModBlocks;
import org.modogthedev.pollution.registry.ModConfig;
import org.modogthedev.pollution.registry.ModParticles;
import org.modogthedev.pollution.util.PollutionClipContext;
import org.slf4j.Logger;

import java.util.List;

public class PollutionEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_GAS_AMOUNT = SynchedEntityData.defineId(PollutionEntity.class, EntityDataSerializers.INT);
    private static final Logger LOGGER = LogUtils.getLogger();
    public boolean filtered = false;
    private int COLLIDE_TICKS = 0;
    private boolean tryFit = false;
    private boolean initialised = false;
    private BlockPos source = this.blockPosition();

    @Override
    public void kill() {
        super.kill();
    }

    public PollutionEntity(EntityType type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_GAS_AMOUNT, 1);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        ListTag list = tag.getList("pollution", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag pollutionTag = (CompoundTag) t;
            this.entityData.set(DATA_GAS_AMOUNT, pollutionTag.getInt("pollution"));
            source = new BlockPos(pollutionTag.getInt("x"), 100, pollutionTag.getInt("z"));
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        CompoundTag pollutionTag = new CompoundTag();
        pollutionTag.putInt("x", source.getX());
        pollutionTag.putInt("z", source.getZ());
        pollutionTag.putInt("pollution", (this.entityData.get(DATA_GAS_AMOUNT)));
        list.add(pollutionTag);
        tag.put("pollution", list);
    }

    @Override
    public boolean save(CompoundTag tag) {
        ListTag list = new ListTag();
        CompoundTag pollutionTag = new CompoundTag();
        pollutionTag.putInt("x", source.getX());
        pollutionTag.putInt("z", source.getZ());
        pollutionTag.putInt("pollution", (this.entityData.get(DATA_GAS_AMOUNT)));
        list.add(pollutionTag);
        tag.put("pollution", list);
        return super.save(tag);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void tick() {
        if (!initialised) {
            source = this.blockPosition();
            changeWorldCurrentPollution(1, source);
            initialised = true;
        }
        this.setNoGravity(false);
        // Sky Removal
        if (this.getY() > 200) {
            if (random.nextIntBetweenInclusive(0, 40) == 40) {
                this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) - 1);
                changeWorldPollution(1);
                if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                    remove();
                }
            }
        }
        // Merge
        List<PollutionEntity> combineList = this.level.getEntitiesOfClass(PollutionEntity.class, this.getBoundingBox().inflate(1.5));
        if (this.entityData.get(DATA_GAS_AMOUNT) < 10) {
            if (combineList.size() > 1) {
                for (int i = 1; i < (combineList.size()); i++) {
                    if (combineList.get(i).entityData.get(DATA_GAS_AMOUNT) <= this.entityData.get(DATA_GAS_AMOUNT)) {
                        if (combineList.get(i) != this) {
                            if (!combineList.get(i).initialised) {
                                source = this.blockPosition();
                                changeWorldCurrentPollution(1, source);
                                initialised = true;
                            }
                            if (this.entityData.get(DATA_GAS_AMOUNT) + combineList.get(i).entityData.get(DATA_GAS_AMOUNT) > 10) {
                                combineList.get(i).entityData.set(DATA_GAS_AMOUNT, (combineList.get(i).entityData.get(DATA_GAS_AMOUNT)) + (this.entityData.get(DATA_GAS_AMOUNT) - 10));
                                this.entityData.set(DATA_GAS_AMOUNT, 10);
                            } else {
                                this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) + combineList.get(i).entityData.get(DATA_GAS_AMOUNT));
                                combineList.get(i).remove();
                            }
                            this.setPos(this.blockPosition().getX() + .5, (this.blockPosition().getY()), this.blockPosition().getZ() + .5);
                            move(new Vec3(0, -.1, 0));
                        }
                    }
                }
            }
        }
        // Collision Effects
        if (this.getEntityData().get(DATA_GAS_AMOUNT) > 7) {
            List<LivingEntity> entitiesAffected = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
            if (entitiesAffected.size() < 1) {
                COLLIDE_TICKS = 0;
            }
            for (LivingEntity livingEntity : entitiesAffected) {
                COLLIDE_TICKS++;
                if (COLLIDE_TICKS > 20) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 80, 0), this);
                    if (COLLIDE_TICKS > 180) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 140, 0), this);
                        if (COLLIDE_TICKS > 440) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0), this);
                        }
                    }
                }
            }
        }
        // Block Modification Data
        BlockHitResult hitResult = level.clip(new PollutionClipContext(this.position(), this.position().add(new Vec3(0, -25, 0)),
                PollutionClipContext.PollutionCollides.POLLUTION_COLLIDES, ClipContext.Fluid.NONE, null, ClipContext.Block.COLLIDER));
        BlockState resultState = this.level.getBlockState(hitResult.getBlockPos());
        // Ground Pollution
        if (resultState.getBlock() == Blocks.GRASS_BLOCK || resultState.getBlock() == Blocks.FARMLAND) {
            this.level.setBlock(hitResult.getBlockPos(), ModBlocks.POLLUTED_SOIL.get().defaultBlockState(), 3);
            return;
        }
        // Passive Decay
        List<PollutionEntity> particleAmount = this.level.getEntitiesOfClass(PollutionEntity.class, this.getBoundingBox().inflate(10));
        int amount = particleAmount.size();
        if (!filtered && random.nextIntBetweenInclusive(0, Math.abs(ModConfig.MAX_POLLUTION.get() - Math.max(0, Math.min(ModConfig.MAX_POLLUTION.get() - 1, amount * ModConfig.POLLUTION_AMOUNT.get())))) == 1) {
            this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) - 1);
            changeWorldPollution(1);
            if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                remove();
            }
        }
        // Plant Transfer
        if (resultState.getBlock() instanceof CropBlock) {
            if (random.nextIntBetweenInclusive(0, 40) == 1) {
                this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) - 1);
                BonemealableBlock bonemealableblock = (BonemealableBlock) resultState.getBlock();
                bonemealableblock.performBonemeal((ServerLevel) this.getLevel(), this.random, hitResult.getBlockPos(), resultState);
                changeWorldCurrentPollution(-1, source);
                if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                    remove();
                }
            }
        }
        // Leaf Dissolve
        if (this.level.getBlockState(this.blockPosition()).getBlock() instanceof LeavesBlock) {
            if (random.nextIntBetweenInclusive(0, 400) == 0) {
                    this.level.setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 3);
            }
            this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) - 1);
            this.filtered = true;
            changeWorldCurrentPollution(-1, source);
            if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                this.kill();
            }
        }
        // Particles
        if (this.level.isClientSide) {
            if (this.getEntityData().get(DATA_GAS_AMOUNT) > 1) {
                if (random.nextIntBetweenInclusive(0, amount * ModConfig.SMALL_PARTICLE_AMOUNT.get() + 8) == 1 && ModConfig.SMALL_PARTICLE_ENABLED.get()) {
                    this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
                if (this.getEntityData().get(DATA_GAS_AMOUNT) > 3 && random.nextIntBetweenInclusive(0, amount * ModConfig.LARGE_PARTICLE_AMOUNT.get() + 40) == 1 && ModConfig.LARGE_PARTICLE_ENABLED.get()) {
                    this.level.addParticle(ModParticles.FOG_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z);
                }
            }
        }
        // Movement
        this.setDeltaMovement(this.getDeltaMovement().x / 1.1, this.getDeltaMovement().y + .0005, this.getDeltaMovement().z / 1.1);
        this.setDeltaMovement(this.getDeltaMovement().add((double) random.nextIntBetweenInclusive(-100, 100) / 10000, 0, (double) random.nextIntBetweenInclusive(-100, 100) / 10000));
        move(this.getDeltaMovement());
    }

    public void move(Vec3 vec3) {
        tryFit = true;
        this.refreshDimensions();
        BlockHitResult hitResult = level.clip(new PollutionClipContext(this.position(), this.position().add(this.getDeltaMovement()),
                PollutionClipContext.PollutionCollides.POLLUTION_COLLIDES, ClipContext.Fluid.NONE, null, ClipContext.Block.COLLIDER));

        this.setPos(hitResult.getLocation());
        tryFit = false;
        this.refreshDimensions();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        if (p_20303_.getClass() == PollutionEntity.class) {
            return canBeCollidedWith(true);
        }
        return canBeCollidedWith(false);
    }

    private int getCurrentPollution() {
        return ClientWorldPollutionData.getChunkCurrentPollution();
    }

    public boolean canBeCollidedWith(Boolean canBeCollided) {
        return canBeCollided;
    }

    @Override
    public void push(@NotNull Entity p_20293_) {
        super.push(this);
    }

    public void remove() {
        if (!initialised) {
            changeWorldCurrentPollution(-1, source);
        }
        this.kill();
    }

    private void changeWorldPollution(int amount) {
        if (level.isClientSide) {
            return;
        } else {
            ModWorldPollution.get(this.level).changePollution(this.blockPosition(), amount);
            changeWorldCurrentPollution(-1, source);
        }
    }

    private void changeWorldCurrentPollution(int amount, BlockPos pos) {
        if (level.isClientSide) {
            return;
        } else {
            ModWorldPollution.get(this.level).changeCurrentPollution(pos, amount);
            if (ModWorldPollution.get(this.level).getCurrentPollution(pos) < 0) {
                ModWorldPollution.get(this.level).changeCurrentPollution(pos, -ModWorldPollution.get(this.level).getCurrentPollution(pos));
            }
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose p_19975_) {
        if (tryFit) {
            return super.getDimensions(p_19975_).scale((float) (this.entityData.get(DATA_GAS_AMOUNT) * 0.18));
        } else {
            return super.getDimensions(p_19975_).scale((float) (this.entityData.get(DATA_GAS_AMOUNT) * 0.6));
        }
    }
}
