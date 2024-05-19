package org.modogthedev.pollution.registry;

import com.simibubi.create.AllBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.modogthedev.pollution.Pollution;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.modogthedev.pollution.block.PollutionSensor;


public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Pollution.MODID);
        public static final RegistryObject<Block> POLLUTED_SOIL = BLOCKS.register("polluted_soil", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
        public static final RegistryObject<Block> POLLUTION_SENSOR = BLOCKS.register("pollution_sensor", () -> new PollutionSensor(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
}