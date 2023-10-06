package org.modogthedev.pollution.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.client.models.PollutionEntityModel;
import org.modogthedev.pollution.main.PollutionEntity;

public class PollutionEntityRender extends EntityRenderer<PollutionEntity, PollutionEntityModel> {
    private  static final ResourceLocation TEXTURE = new ResourceLocation(Pollution.MODID, "textures/entities/pollution.png")
    protected PollutionEntityRender(EntityRendererProvider.Context p_174008_) {
        super(p_174008_, new PollutionEntityModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(PollutionEntity p_114482_) {
        return TEXTURE;
    }
}
