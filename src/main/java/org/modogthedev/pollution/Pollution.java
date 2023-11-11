package org.modogthedev.pollution;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.modogthedev.pollution.init.Messages;
import org.modogthedev.pollution.init.EntityInit;
import org.modogthedev.pollution.init.TileEntityInit;
import org.modogthedev.pollution.main.ModBlocks;
import org.modogthedev.pollution.main.ModItems;
import org.modogthedev.pollution.main.ModParticles;
import org.modogthedev.pollution.main.worldPollution.ChunkPollutionManager;
import org.modogthedev.pollution.main.worldPollution.PollutionEvents;
import org.modogthedev.pollution.util.Config;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Pollution.MODID)
public class Pollution {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "pollution";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Pollution() {
        Config.register();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
        Messages.register();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        EntityInit.ENITITES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        setup();

    }
    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(PollutionEvents::onWorldTick);
        bus.addListener(ChunkPollutionManager::onChunkTick);
        bus.addListener(ChunkPollutionManager::chunkLoad);
        bus.addListener(ChunkPollutionManager::chunkUnload);
        bus.addListener(ChunkPollutionManager::worldLoad);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Server starting...");
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("Client Starting...");
        }
    }
}