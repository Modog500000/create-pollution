package org.modogthedev.pollution.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.commands.ChunkPollutionCommand;
import org.modogthedev.pollution.main.ModParticles;
import org.modogthedev.pollution.main.customParticles.FogParticle;

@Mod.EventBusSubscriber(modid = Pollution.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.FOG_PARTICLE.get(),
                FogParticle.Provider::new);
    }
}
