package org.modogthedev.pollution.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.main.ModParticles;
import org.modogthedev.pollution.main.customParticles.FogParticle;

@Mod.EventBusSubscriber(modid = Pollution.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.FOG_PARTICLE.get(),
                FogParticle.Provider::new);
    }
}
