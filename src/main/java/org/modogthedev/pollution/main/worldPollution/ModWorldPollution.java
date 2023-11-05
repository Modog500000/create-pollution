package org.modogthedev.pollution.main.worldPollution;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.pollution.init.Messages;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ModWorldPollution extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<ChunkPos, WorldPollution> pollutionMap = new HashMap<>();
    private final Map<ChunkPos, WorldCurrentPollution> pollutionCurrentMap = new HashMap<>();

    private int counter = 0;

    @Nonnull
    public static ModWorldPollution get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        return storage.computeIfAbsent(ModWorldPollution::new, ModWorldPollution::new, "pollutionmanager");
    }

    @NotNull
    private WorldPollution getPollutionInternal(BlockPos pos) {
        // Get the mana at a certain chunk. If this is the first time then we fill in the manaMap using computeIfAbsent
        ChunkPos chunkPos = new ChunkPos(pos);
        return pollutionMap.computeIfAbsent(chunkPos, cp -> new WorldPollution(0));
    }    @NotNull
    private WorldCurrentPollution getCurrentPollutionInternal(BlockPos pos) {
        // Get the mana at a certain chunk. If this is the first time then we fill in the manaMap using computeIfAbsent
        ChunkPos chunkPos = new ChunkPos(pos);
        return pollutionCurrentMap.computeIfAbsent(chunkPos, cp -> new WorldCurrentPollution(0));
    }
    public void changePollution(BlockPos pos, int amount) {
        WorldPollution pollution = getPollutionInternal(pos);
        int present = pollution.getPollution();
            pollution.setPollution(present+amount);
            setDirty();
    }    public void changeCurrentPollution(BlockPos pos, int amount) {
        WorldCurrentPollution pollution = getCurrentPollutionInternal(pos);
        int present = pollution.getPollution();
            pollution.setPollution(present+amount);
            setDirty();
    }
    public void tick(Level level) {
        counter--;

        if (counter <= 0) {
            counter = 10;

            // Synchronize the pollution to the players in this world
            // todo expansion: keep the previous data that was sent to the player and only send if changed
            level.players().forEach(player -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    int chunkPollution = getPollution(serverPlayer.blockPosition());
                    int chunkCurrentPollution = getCurrentPollution(serverPlayer.blockPosition());
                    Messages.sendToPlayer(new PacketSyncWorldPollutionToClient(chunkPollution), serverPlayer);
                    Messages.sendToPlayer(new PacketSyncWorldCurrentPollutionToClient(chunkCurrentPollution), serverPlayer);

                }
            });
        }
    }
    public int getPollution(BlockPos pos) {
        WorldPollution pollution = getPollutionInternal(pos);
        return pollution.getPollution();
    }    public int getCurrentPollution(BlockPos pos) {
        WorldCurrentPollution pollution = getCurrentPollutionInternal(pos);
        return pollution.getPollution();
    }
    public ModWorldPollution() {
    }
    public ModWorldPollution(CompoundTag tag) {
        ListTag list = tag.getList("worldpollution", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag pollutionTag = (CompoundTag) t;
            WorldPollution worldPollution = new WorldPollution(pollutionTag.getInt("worldpollution"));
            ChunkPos pos = new ChunkPos(pollutionTag.getInt("x"), pollutionTag.getInt("z"));
            pollutionMap.put(pos, worldPollution);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        pollutionMap.forEach((chunkPos, worldPollution) -> {
            CompoundTag pollutionTag = new CompoundTag();
            pollutionTag.putInt("x", chunkPos.x);
            pollutionTag.putInt("z", chunkPos.z);
            pollutionTag.putInt("worldpollution", worldPollution.getPollution());
            list.add(pollutionTag);
        });
        tag.put("worldpollution", list);
        return tag;
    }
}