package org.modogthedev.pollution.main.worldPollution;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.modogthedev.pollution.main.ModConfig;
@OnlyIn(Dist.CLIENT)
public class PollutionOverlay {
    public static final IGuiOverlay HUD_POLLUTION = (gui, poseStack, partialTicks, width, height) -> {
        String toDisplay = String.valueOf(ClientWorldPollutionData.getChunkPollution()/1000) + " ppm";
        String toDisplay2 = String.valueOf(ClientWorldPollutionData.getChunkCurrentPollution()) + " Current";
        int x = ModConfig.POLLUTION_HUD_Y.get();
        int y = ModConfig.POLLUTION_HUD_Y.get();
        if (GogglesItem.isWearingGoggles(Minecraft.getInstance().player)) {
            if (x >= 0 && y >= 0) {
                gui.getFont().draw(poseStack, toDisplay, x, y, ModConfig.POLLUTION_HUD_COLOR.get());
                gui.getFont().draw(poseStack, toDisplay2, x, y + 20, ModConfig.POLLUTION_HUD_COLOR.get());
            }
        }
    };
}
