package org.modogthedev.pollution.main;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;
import org.modogthedev.pollution.Pollution;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Pollution.MODID);
        public static final RegistryObject<Block> POLLUTED_SOIL = BLOCKS.register("polluted_soil", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
}