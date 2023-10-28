package org.modogthedev.pollution.main.worldPollution;

import net.minecraftforge.network.NetworkEvent;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class PacketSyncWorldPollutionToClient {
    private final int pollution;
    public PacketSyncWorldPollutionToClient(int pollution) {
        this.pollution = pollution;
    }

    public PacketSyncWorldPollutionToClient(FriendlyByteBuf buf) {
        pollution = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(pollution);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft) because
            // this packet needs to be available server-side too
            ClientWorldPollutionData.set(pollution);
        });
        return true;
    }
}
