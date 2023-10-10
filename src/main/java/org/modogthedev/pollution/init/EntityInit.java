package org.modogthedev.pollution.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.modogthedev.pollution.Pollution;
import org.modogthedev.pollution.main.PollutionEntity;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENITITES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Pollution.MODID);

    public  static  final RegistryObject<EntityType<PollutionEntity>> POLLUTION_ENTITY = ENITITES.register("pollution", ()-> EntityType.Builder.of(PollutionEntity::new, MobCategory.MISC).sized(0.5F,0.5F).build(new ResourceLocation(Pollution.MODID, "pollution").toString()));
}