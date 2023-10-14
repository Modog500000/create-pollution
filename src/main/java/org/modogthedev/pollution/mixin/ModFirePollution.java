package org.modogthedev.pollution.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.dimension.DimensionType;
import org.modogthedev.pollution.init.EntityInit;
import org.modogthedev.pollution.main.PollutionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public abstract class ModFirePollution{

    @Inject(method = "tick", at = @At("HEAD"))
    public void create$firePolluteMethod(BlockState p_221160_, ServerLevel level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        if (randomSource.nextFloat() < 0.7F) {
            if (level.dimension() == Level.OVERWORLD) {
                PollutionEntity pollutionEntity = EntityInit.POLLUTION_ENTITY.get().create(level);
                pollutionEntity.setPos(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
                level.addFreshEntity(pollutionEntity);
            }
        }
    }
}
