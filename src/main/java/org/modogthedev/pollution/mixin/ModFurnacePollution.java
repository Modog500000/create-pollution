package org.modogthedev.pollution.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.pollution.init.EntityInit;
import org.modogthedev.pollution.main.PollutionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class ModFurnacePollution {
    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void create$furnacePolluteMethod(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity p_155017_, CallbackInfo ci) {
        assert level != null;
        if (state.hasProperty(AbstractFurnaceBlock.LIT)) {
            if ((float) RandomSource.create().nextInt(0, 1000) /100 < 0.1F) {
                PollutionEntity pollutionEntity = EntityInit.POLLUTION_ENTITY.get().create(level);
                assert pollutionEntity != null;
                pollutionEntity.setPos(pos.getX()+.5, pos.getY()+1, pos.getZ()+.5);
                level.addFreshEntity(pollutionEntity);
            }
        }
    }
}