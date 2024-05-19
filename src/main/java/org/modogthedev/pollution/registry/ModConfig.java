package org.modogthedev.pollution.registry;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static ForgeConfigSpec.IntValue POLLUTION_AMOUNT;
    public static ForgeConfigSpec.IntValue MAX_POLLUTION;

    public static ForgeConfigSpec.IntValue POLLUTION_HUD_X;
    public static ForgeConfigSpec.IntValue POLLUTION_HUD_Y;
    public static ForgeConfigSpec.IntValue POLLUTION_HUD_COLOR;
    public static ForgeConfigSpec.IntValue SMALL_PARTICLE_AMOUNT;
    public static ForgeConfigSpec.IntValue LARGE_PARTICLE_AMOUNT;
    public static ForgeConfigSpec.BooleanValue SMALL_PARTICLE_ENABLED;
    public static ForgeConfigSpec.BooleanValue LARGE_PARTICLE_ENABLED;
    public static ForgeConfigSpec.BooleanValue DEBUG;

    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for the pollution system").push("pollution");
        POLLUTION_AMOUNT = SERVER_BUILDER
                .comment("Higher numbers decreases pollution")
                .defineInRange("pollutionAmount", 15, 1, Integer.MAX_VALUE);
        MAX_POLLUTION = SERVER_BUILDER
                .comment("Higher numbers decreases max pollution")
                .defineInRange("maxPollution", 1000, 1, Integer.MAX_VALUE);

        SERVER_BUILDER.pop();
    }

    public static void registerClientConfig(ForgeConfigSpec.Builder CLIENT_BUILDER) {
        CLIENT_BUILDER.comment("Settings for the pollution system").push("pollution");
        CLIENT_BUILDER.comment("- Hud settings").push("pollution");

        POLLUTION_HUD_X = CLIENT_BUILDER
                .comment("X location of the pollution hud")
                .defineInRange("pollutionHudX", 10, -1, Integer.MAX_VALUE);
        POLLUTION_HUD_Y = CLIENT_BUILDER
                .comment("Y location of the pollution hud")
                .defineInRange("pollutionHudY", 10, -1, Integer.MAX_VALUE);
        POLLUTION_HUD_COLOR = CLIENT_BUILDER
                .comment("Color of the pollution hud")
                .defineInRange("pollutionHudColor", 0xffffffff, Integer.MIN_VALUE, Integer.MAX_VALUE);

        CLIENT_BUILDER.comment("- Particle settings").push("Particles");
        SMALL_PARTICLE_AMOUNT = CLIENT_BUILDER
                .comment("Amount of small particles")
                .defineInRange("smallParticleAmount", 2, -1, Integer.MAX_VALUE);
        LARGE_PARTICLE_AMOUNT = CLIENT_BUILDER
                .comment("Amount of large particles")
                .defineInRange("largeParticleAmount", 3, -1, Integer.MAX_VALUE);
        SMALL_PARTICLE_ENABLED = CLIENT_BUILDER
                .comment("Small Particles Enabled").define("smallParticleEnabled", true);
        LARGE_PARTICLE_ENABLED = CLIENT_BUILDER
                .comment("Large Particles Enabled").define("largeParticleEnabled", true);
        DEBUG = CLIENT_BUILDER
                .comment("Debug").define("debug", false);

        CLIENT_BUILDER.pop();
    }
}
