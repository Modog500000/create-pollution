package org.modogthedev.pollution.events;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.client.models.PollutionEntityModel;
import org.modogthedev.pollution.client.renderer.PollutionEntityRender;
import org.modogthedev.pollution.init.EntityInit;

@Mod.EventBusSubscriber(modid= Pollution.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {

    @SubscribeEvent
    public static void entityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.POLLUTION_ENTITY.get(), PollutionEntityRender::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PollutionEntityModel.LAYER_LOCATION, PollutionEntityModel::createBodyLayer);
    }
}