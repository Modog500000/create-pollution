package org.modogthedev.pollution.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.pollution.registry.EntityInit;
import org.modogthedev.pollution.entities.PollutionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class ModCampfirePollution {
    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void create$campfirePolluteMethod(Level level, BlockPos pos, BlockState state, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci) {
        if (Math.floor(Math.random() * 80) == 0) {
            if (level.dimension() == Level.OVERWORLD) {
                PollutionEntity pollutionEntity = EntityInit.POLLUTION_ENTITY.get().create(level);
                pollutionEntity.setPos(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
                level.addFreshEntity(pollutionEntity);
            }
        }
    }
}
