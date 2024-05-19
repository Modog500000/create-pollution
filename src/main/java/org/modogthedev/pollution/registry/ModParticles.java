package org.modogthedev.pollution.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.modogthedev.pollution.Pollution;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Pollution.MODID);

    public static final RegistryObject<SimpleParticleType> FOG_PARTICLE =
            PARTICLE_TYPES.register("fog_particle", () -> new SimpleParticleType(true));
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
