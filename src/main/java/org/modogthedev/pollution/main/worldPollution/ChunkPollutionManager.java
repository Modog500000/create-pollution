package org.modogthedev.pollution.main.worldPollution;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChunkPollutionManager {
    public static List<ChunkPos> chunkPosList = new ArrayList<ChunkPos>();
    public static List<ChunkPos> toRemove = new ArrayList<ChunkPos>();
    public static List<ChunkPos> toAdd = new ArrayList<ChunkPos>();
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean HOLD = false;

    public static void onChunkTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide){
            return;
        }
        if (!HOLD) {
            for (ChunkPos chunkPos : chunkPosList) {
                BlockPos pos = chunkPos.getMiddleBlockPosition(64);
                Level level = event.level;
                if (ModWorldPollution.get(level).getPollution(pos) > 0) {
                    if (Math.floor(Math.random() * 400000) == 0) {
                        ModWorldPollution.get(level).softChangePollution(pos, -1);
                    }
                    if (Math.floor(Math.random() * 400) == 0) {
                        int random1 = 0;
                        if (Math.random()>0.33) {
                            random1 = 16;
                        } else if (Math.random()>0.33){
                            random1 = -16;
                        }
                        int random2 = 0;
                        if (Math.random()>0.33) {
                            random2 = 16;
                        } else if (Math.random()>0.33){
                            random2 = -16;
                        }
                        BlockPos newPos = new BlockPos(pos.getX() + (random1), 64, pos.getZ() + (random2));
                        double moveAmount = Math.ceil(ModWorldPollution.get(level).getPollution(pos)*0.001);
                        ModWorldPollution.get(level).softChangePollution(pos, (int) -moveAmount);
                        ModWorldPollution.get(level).changePollution(newPos, (int) moveAmount);
                    }
                }
            }
        }
        chunkPosList.removeAll(toRemove);
        chunkPosList.addAll(toAdd);
        toAdd.clear();
        toRemove.clear();
    }
    public static void chunkLoad(ChunkEvent.Load event) {
        toAdd.add(event.getChunk().getPos());
    }
    public static void chunkUnload(ChunkEvent.Unload event) {
        toRemove.add(event.getChunk().getPos());
    }
    public static void worldLoad(LevelEvent.Load event) {
        chunkPosList.clear();
    }
}
