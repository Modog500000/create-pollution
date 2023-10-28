package org.modogthedev.pollution.main.worldPollution;

import com.mojang.logging.LogUtils;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;

public class PollutionEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        // Don't do anything client side
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        ModWorldPollution manager = ModWorldPollution.get(event.level);
        manager.tick(event.level);
    }
}
