package org.modogthedev.pollution.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.pollution.init.EntityInit;
import org.modogthedev.pollution.main.PollutionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlazeBurnerBlockEntity.class)
public abstract class ModBlazeBurnerPollution extends SmartBlockEntity {
    protected int remainingBurnTime;
    public ModBlazeBurnerPollution(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    private void create$burnerPolluteMethod(CallbackInfo ci) {
                super.tick();
                BlockPos pos = this.worldPosition;
                Level level = this.level;
        assert level != null;
        if (this.remainingBurnTime > 0) {
            if ((float) RandomSource.create().nextInt(0, 1000) /100 < 0.1F) {
                PollutionEntity pollutionEntity = EntityInit.POLLUTION_ENTITY.get().create(level);
                pollutionEntity.setPos(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
                level.addFreshEntity(pollutionEntity);
            }
        }
    }
}
