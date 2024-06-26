package org.modogthedev.pollution.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.system.worldPollution.PacketSyncWorldCurrentPollutionToClient;
import org.modogthedev.pollution.system.worldPollution.PacketSyncWorldPollutionToClient;

public class Messages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Pollution.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncWorldPollutionToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncWorldPollutionToClient::new)
                .encoder(PacketSyncWorldPollutionToClient::toBytes)
                .consumerMainThread(PacketSyncWorldPollutionToClient::handle)
                .add();
        net.messageBuilder(PacketSyncWorldCurrentPollutionToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncWorldCurrentPollutionToClient::new)
                .encoder(PacketSyncWorldCurrentPollutionToClient::toBytes)
                .consumerMainThread(PacketSyncWorldCurrentPollutionToClient::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
