package org.modogthedev.pollution.mixin;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.ForgeHooksClient;
import org.modogthedev.pollution.client.renderer.PollutionRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "setupFog", at = @At(value = "HEAD"), cancellable = true)
    private static void injected(Camera pCamera, FogRenderer.FogMode pFogMode, float pFarPlaneDistance, boolean p_234176_, float p_234177_, CallbackInfo ci) {
        PollutionRenderer.farPlaneDist = pFarPlaneDistance;
        Level level = (Level) (Object) Minecraft.getInstance().level;
        float[] fogDistance = PollutionRenderer.getFogDistance();
        FogType fogtype = pCamera.getFluidInCamera();
        Entity entity = pCamera.getEntity();
        PollutionRenderer.FogData fogrenderer$fogdata = new PollutionRenderer.FogData(pFogMode);
        if (fogtype == FogType.LAVA) {
            if (entity.isSpectator()) {
                fogrenderer$fogdata.start = -8.0F;
                fogrenderer$fogdata.end = pFarPlaneDistance * 0.5F;
            } else if (entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                fogrenderer$fogdata.start = 0.0F;
                fogrenderer$fogdata.end = 3.0F;
            } else {
                fogrenderer$fogdata.start = 0.25F;
                fogrenderer$fogdata.end = 1.0F;
            }
        } else if (fogtype == FogType.POWDER_SNOW) {
            if (entity.isSpectator()) {
                fogrenderer$fogdata.start = -8.0F;
                fogrenderer$fogdata.end = pFarPlaneDistance * 0.5F;
            } else {
                fogrenderer$fogdata.start = 0.0F;
                fogrenderer$fogdata.end = 2.0F;
            }
        } else if (fogtype == FogType.WATER) {
            fogrenderer$fogdata.start = -8.0F;
            fogrenderer$fogdata.end = 96.0F;
            if (entity instanceof LocalPlayer) {
                LocalPlayer localplayer = (LocalPlayer) entity;
                fogrenderer$fogdata.end *= Math.max(0.25F, localplayer.getWaterVision());
                Holder<Biome> holder = localplayer.getLevel().getBiome(localplayer.blockPosition());
                if (holder.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                    fogrenderer$fogdata.end *= 0.85F;
                }
            }

            if (fogrenderer$fogdata.end > pFarPlaneDistance) {
                fogrenderer$fogdata.end = pFarPlaneDistance;
                fogrenderer$fogdata.shape = FogShape.CYLINDER;
            }
        } else if (p_234176_) {
            fogrenderer$fogdata.start = pFarPlaneDistance * 0.05F;
            fogrenderer$fogdata.end = Math.min(pFarPlaneDistance, 192.0F) * 0.5F;
        } else if (pFogMode == FogRenderer.FogMode.FOG_SKY) {
            fogrenderer$fogdata.start = 0.0F;
            fogrenderer$fogdata.end = pFarPlaneDistance;
            fogrenderer$fogdata.shape = FogShape.CYLINDER;
        } else {
            float f = Mth.clamp(pFarPlaneDistance / 10.0F, 4.0F, 64.0F);
            fogrenderer$fogdata.start = fogDistance[0];
            fogrenderer$fogdata.end = fogDistance[1];
            fogrenderer$fogdata.shape = FogShape.CYLINDER;
        }

        RenderSystem.setShaderFogStart(fogrenderer$fogdata.start);
        RenderSystem.setShaderFogEnd(fogrenderer$fogdata.end);
        RenderSystem.setShaderFogShape(fogrenderer$fogdata.shape);
        ForgeHooksClient.onFogRender(pFogMode, fogtype, pCamera, p_234177_, pFarPlaneDistance, fogrenderer$fogdata.start, fogrenderer$fogdata.end, fogrenderer$fogdata.shape);
        ci.cancel();
    }
}
