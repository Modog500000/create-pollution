package org.modogthedev.pollution.client.renderer;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import org.modogthedev.pollution.system.worldPollution.ClientWorldPollutionData;

import java.awt.*;

public class PollutionRenderer {
    public static Color color = new Color(255, 255, 255, 255);
    public static Color targetColor = new Color(255, 255, 255, 255);
    public static final int amountToPollute = 500;
    public static float[] fogData = new float[2];
    public static float realMinFog;
    public static float farPlaneDist;

    public static void renderRain() {

    }

    public static Color getRainColor() {
        Level level = Minecraft.getInstance().level;
        int amount = ClientWorldPollutionData.getChunkPollution();
        Color normalColor = Color.WHITE;
        Color pollutedColor = new Color(147, 157, 65, 255);
        if (amount > 25) {
            targetColor = pollutedColor;
        } else {
            targetColor = normalColor;
        }
        return color;
    }

    public static void tick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            int amount = ClientWorldPollutionData.getChunkPollution();
            int red = color.getRed();
            int blue = color.getBlue();
            int green = color.getGreen();
            int targetRed = targetColor.getRed();
            int targetBlue = targetColor.getBlue();
            int targetGreen = targetColor.getGreen();
            if (targetRed > red) {
                red++;
            }
            if (targetRed < red) {
                red--;
            }
            if (targetBlue > blue) {
                blue++;
            }
            if (targetBlue < blue) {
                blue--;
            }
            if (targetGreen > green) {
                red++;
            }
            if (targetGreen < green) {
                green--;
            }
            color = new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue), 255);
            fogData[0] = 0f;
            float f = Mth.clamp(farPlaneDist / 10.0F, 4.0F, 64.0F);
            float fogTarget = Math.max(10, (farPlaneDist - f - (amount / 400f)));
            int easeTime = 100;
            realMinFog = ((fogData[1]*(easeTime-1))+fogTarget)/easeTime;
            fogData[1] = realMinFog;
            if (Minecraft.getInstance().player.isSpectator() || Minecraft.getInstance().player.isScoping()) {
                fogData[1] = farPlaneDist-f;
            }
        }
    }

    public static float[] getFogDistance() {
        return fogData;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FogData {
        public final FogRenderer.FogMode mode;
        public float start;
        public float end;
        public FogShape shape;

        public FogData(FogRenderer.FogMode pMode) {
            this.shape = FogShape.SPHERE;
            this.mode = pMode;
        }
    }
}
