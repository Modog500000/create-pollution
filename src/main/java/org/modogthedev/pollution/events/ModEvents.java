package org.modogthedev.pollution.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.commands.ChunkPollutionCommand;

@Mod.EventBusSubscriber(modid = Pollution.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ChunkPollutionCommand((event.getDispatcher()));

        ConfigCommand.register(event.getDispatcher());
    }
}
