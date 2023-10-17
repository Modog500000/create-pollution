package org.modogthedev.pollution.main;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

public class PollutionEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_GAS_AMOUNT = SynchedEntityData.defineId(PollutionEntity.class, EntityDataSerializers.INT);

    private int COLLIDE_TICKS = 0;
    public PollutionEntity(EntityType type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_GAS_AMOUNT, 1);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag p_20052_) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag p_20139_) {
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
        this.fireImmune();
        // Movement
        this.setNoGravity(false);
        this.setDeltaMovement(this.getDeltaMovement().x / 1.1, this.getDeltaMovement().y, this.getDeltaMovement().z / 1.1);
        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y + .00025, this.getDeltaMovement().z);
        if (this.getY() > 200) {
            if (random.nextIntBetweenInclusive(0,40) == 40) {
                this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT)-1);
                if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                    this.kill();
                }
            }
        }
        // Merge
        List<PollutionEntity> combineList = this.level.getEntitiesOfClass(PollutionEntity.class, this.getBoundingBox().inflate(0.1));
        if (this.entityData.get(DATA_GAS_AMOUNT) < 10) {
            if (combineList.size() > 1) {
                for (int i = 1; i < (combineList.size()); i++) {
                    if (combineList.get(i).entityData.get(DATA_GAS_AMOUNT) <= this.entityData.get(DATA_GAS_AMOUNT)) {
                        if (combineList.get(i) != this) {
                            if (this.entityData.get(DATA_GAS_AMOUNT) + combineList.get(i).entityData.get(DATA_GAS_AMOUNT) > 10) {
                                combineList.get(i).entityData.set(DATA_GAS_AMOUNT, (combineList.get(i).entityData.get(DATA_GAS_AMOUNT)) + (this.entityData.get(DATA_GAS_AMOUNT) - 10));
                                this.entityData.set(DATA_GAS_AMOUNT, 10);
                            } else {
                                this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT) + combineList.get(i).entityData.get(DATA_GAS_AMOUNT));
                                combineList.get(i).kill();
                            }
                            this.setPos(this.blockPosition().getX() + .5, (this.blockPosition().getY()), this.blockPosition().getZ() + .5);
                            this.setDeltaMovement(this.getDeltaMovement().add(0, -.001, 0));
                        }
                    }
                }
            }
        }
        // Collision Effects
        if (this.getEntityData().get(DATA_GAS_AMOUNT) > 7) {
            List<LivingEntity> entitiesAffected = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1));
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
        // Ground Pollution
        if (this.getEntityData().get(DATA_GAS_AMOUNT) > 3 && this.level.isRaining() && this.level.isRainingAt(this.blockPosition()) && random.nextIntBetweenInclusive(0,100) == 100) {
            for (int i = (int) this.getY(); i > 0; i--) {
                if (this.level.getBlockState(new BlockPos(((int) this.getX()),i,((int) this.getZ()))) == Blocks.GRASS_BLOCK.defaultBlockState() || this.level.getBlockState(new BlockPos(((int) this.getX()),i,((int) this.getZ()))) == Blocks.FARMLAND.defaultBlockState()) {
                    this.level.setBlock(new BlockPos(((int) this.getX()),i,((int) this.getZ())), ModBlocks.POLLUTED_SOIL.get().defaultBlockState(), 1);
                    return;
                }
            }
        }
        // Passive Decay
        if (random.nextIntBetweenInclusive(0,1000) == 1000) {
            this.entityData.set(DATA_GAS_AMOUNT, this.entityData.get(DATA_GAS_AMOUNT)-1);
            if (this.entityData.get(DATA_GAS_AMOUNT) < 1) {
                this.kill();
            }
        }
        // Particles
        if (this.getEntityData().get(DATA_GAS_AMOUNT) > 1) {
            this.level.addParticle(ModParticles.FOG_PARTICLE.get(), this.getX(), this.getY(), this.getZ(),0,0,0);
        }
        // Movement
        this.setDeltaMovement(this.getDeltaMovement().add((double) random.nextIntBetweenInclusive(-100,100) /10000,0,(double)random.nextIntBetweenInclusive(-100,100)/10000));
        this.move(MoverType.SELF, this.getDeltaMovement());
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
    public boolean canBeCollidedWith(Boolean canBeCollided) {
        return canBeCollided;
    }
    @Override
    public void push(@NotNull Entity p_20293_) {
        super.push(this);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose p_19975_) {
        return super.getDimensions(p_19975_).scale((float) (this.entityData.get(DATA_GAS_AMOUNT)*0.17));
    }
}