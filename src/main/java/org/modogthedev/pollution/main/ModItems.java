package org.modogthedev.pollution.main;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.pollution.Pollution;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Pollution.MODID);
    public static final CreativeModeTab TAB = new CreativeModeTab(Pollution.MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return POLLUTED_SOIL.get().getDefaultInstance();
        }
    };
    public static final RegistryObject<Item> POLLUTED_SOIL = ITEMS.register("polluted_soil", () -> new BlockItem(ModBlocks.POLLUTED_SOIL.get(), new Item.Properties().tab(ModCreativeModeTabs.TAB)));

}