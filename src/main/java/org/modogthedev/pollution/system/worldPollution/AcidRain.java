package org.modogthedev.pollution.system.worldPollution;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.pollution.registry.ModBlocks;
import org.slf4j.Logger;

public class AcidRain {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void runAcidRain(BlockPos prePos, Level level, int weight) {
        if (level.isRaining()) {
            BlockPos pos = blockHeight(prePos, level);
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (Math.floor(Math.random() * 80 / Math.ceil((double) weight / 1000)) == 0) {
                LOGGER.info("Random Pass");
                if (block instanceof LeavesBlock) {
                    remove(pos, level);
                    LOGGER.info("Remove");
                }
                if (block == Blocks.DIRT || block == Blocks.FARMLAND) {
                    level.setBlock(pos, ModBlocks.POLLUTED_SOIL.get().defaultBlockState(), 1);
                    LOGGER.info("Transform");
                }
            }
        }
    }
    public static void remove(BlockPos pos, Level level) {
        level.setBlock(pos,Blocks.AIR.defaultBlockState(),1);
    }
    public static BlockPos blockHeight(BlockPos prePos, Level level) {
        for (int y = level.getHeight(); y > -64; y--) {
            if (!level.canSeeSky(new BlockPos(prePos.getX(),y,prePos.getZ()))) {
                return new BlockPos(prePos.getX(),y,prePos.getZ());
            }
        }
        return new BlockPos(prePos.getX(),-64,prePos.getZ());
    }
}
