package org.modogthedev.pollution.main;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import org.antlr.v4.codegen.model.Sync;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

public class PollutionEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Integer> DATA_GAS_AMOUNT = SynchedEntityData.defineId(PollutionEntity.class, EntityDataSerializers.INT);
    public PollutionEntity(EntityType type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_GAS_AMOUNT, 1);
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }
    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

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
        this.setNoGravity(false);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().x / 1.1, this.getDeltaMovement().y, this.getDeltaMovement().z / 1.1);
        this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - .001, this.getDeltaMovement().z);
        if (this.getY() > 319) {
            this.kill();
        }
        List<PollutionEntity> combineList = this.level.getEntitiesOfClass(PollutionEntity.class, this.getBoundingBox().inflate(5));

    }
}