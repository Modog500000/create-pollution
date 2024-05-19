package org.modogthedev.pollution.sources;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.modogthedev.pollution.registry.EntityInit;
import org.modogthedev.pollution.entities.PollutionEntity;

public class PollutionSource {
    public void createPollution(ServerLevel level, BlockPos pos) {
            PollutionEntity pollutionEntity = EntityInit.POLLUTION_ENTITY.get().create(level);
            pollutionEntity.setPos(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
            level.addFreshEntity(pollutionEntity);
    }

}
