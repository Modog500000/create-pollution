package org.modogthedev.pollution.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.main.worldPollution.PollutionOverlay;

@Mod.EventBusSubscriber(modid = Pollution.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "pollution_overlay", PollutionOverlay.HUD_POLLUTION);
    }
}
