package org.modogthedev.pollution.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import org.modogthedev.pollution.init.TileEntityInit;
import org.modogthedev.pollution.main.ModConfig;
import org.modogthedev.pollution.main.PollutionEntity;

import java.util.List;

public class PollutionSensorTile extends BlockEntity {
    public static IntegerProperty POWER = BlockStateProperties.POWER;
    public PollutionSensorTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.POLLUTION_SENSOR.get(), pos, state);
    }
    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        PollutionSensorTile tile = (PollutionSensorTile) be;
        List<PollutionEntity> combineList = level.getEntitiesOfClass(PollutionEntity.class, new AABB(pos).inflate(20));
        level.setBlock(pos, state.setValue(POWER,Math.max(0, Math.min(15,combineList.size()))), 1);
    }
}
