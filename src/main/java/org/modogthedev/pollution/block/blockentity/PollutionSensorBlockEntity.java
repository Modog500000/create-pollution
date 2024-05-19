package org.modogthedev.pollution.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import org.modogthedev.pollution.registry.ModBlockEntities;
import org.modogthedev.pollution.entities.PollutionEntity;
import org.modogthedev.pollution.util.TickableBlockEntity;

import java.util.List;

public class PollutionSensorBlockEntity extends BlockEntity implements TickableBlockEntity {
    public static IntegerProperty POWER = BlockStateProperties.POWER;
    public float dialState;
    public float prevDialState;
    public PollutionSensorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POLLUTION_SENSOR.get(), pos, state);
    }
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {

    }

    @Override
    public void tick() {
        List<PollutionEntity> combineList = level.getEntitiesOfClass(PollutionEntity.class, new AABB(getBlockPos()).inflate(20));
        int power = Math.max(0, Math.min(15,combineList.size()));
        prevDialState = this.dialState;
        dialState = power/15f;
        if (!getLevel().isClientSide)
        level.setBlock(getBlockPos(), getBlockState().setValue(POWER,power), 1);
    }
}
