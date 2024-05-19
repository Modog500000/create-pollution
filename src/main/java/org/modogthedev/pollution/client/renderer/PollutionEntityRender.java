package org.modogthedev.pollution.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.client.models.PollutionEntityModel;
import org.modogthedev.pollution.entities.PollutionEntity;

public class PollutionEntityRender extends EntityRenderer<PollutionEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Pollution.MODID, "textures/entities/pollution.png");

    public PollutionEntityRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        PollutionEntityModel<PollutionEntity> model = new PollutionEntityModel<>(ctx.bakeLayer(PollutionEntityModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(PollutionEntity p_114482_) {
        return TEXTURE;
    }

}