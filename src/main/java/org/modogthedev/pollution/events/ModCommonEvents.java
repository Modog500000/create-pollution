package org.modogthedev.pollution.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.commands.ChunkPollutionCommand;
import org.modogthedev.pollution.init.EntityInit;
import org.modogthedev.pollution.main.PollutionEntity;

@Mod.EventBusSubscriber(modid= Pollution.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {

}