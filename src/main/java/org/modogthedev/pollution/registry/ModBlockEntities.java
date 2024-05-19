package org.modogthedev.pollution.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.block.blockentity.PollutionSensorBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Pollution.MODID);

    public static final RegistryObject<BlockEntityType<PollutionSensorBlockEntity>> POLLUTION_SENSOR = TILE_ENTITY_TYPES.register("pollution_sensor",
            () -> BlockEntityType.Builder.of(PollutionSensorBlockEntity::new, ModBlocks.POLLUTION_SENSOR.get()).build(null));

}

