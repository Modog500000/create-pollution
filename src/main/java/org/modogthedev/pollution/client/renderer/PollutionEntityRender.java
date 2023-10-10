package org.modogthedev.pollution.client.renderer;


import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.client.models.PollutionEntityModel;
import org.modogthedev.pollution.main.PollutionEntity;

public class PollutionEntityRender<T> extends EntityRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Pollution.MODID, "textures/entities/pollution.png");

    public PollutionEntityRender(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_114482_) {
        return null;
    }

}