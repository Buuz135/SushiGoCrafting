package com.buuz135.sushigocrafting.proxy;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.crop.CustomCropBlock;
import com.buuz135.sushigocrafting.block.crop.WaterCropBlock;
import com.buuz135.sushigocrafting.item.AmountItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SushiContent {

    public static RegistryObject<Block> block(String id, Supplier<Block> block) {
        return Blocks.REGISTRY.register(id, block);
    }

    public static RegistryObject<Item> basicItem(String id) {
        return Items.REGISTRY.register(id, () -> new Item(new Item.Properties().group(SushiGoCrafting.TAB)));
    }

    public static RegistryObject<Item> amountItem(String id, int minAmount, int maxAmount, int maxCombine) {
        return Items.REGISTRY.register(id, () -> new AmountItem(new Item.Properties().group(SushiGoCrafting.TAB).maxStackSize(1), minAmount, maxAmount, maxCombine));
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup) {
        return Items.REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().group(SushiGoCrafting.TAB)));
    }

    public static class Blocks {

        public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<Block> RICE_CROP = block("rice_crop", () -> new WaterCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.KELP_PLANT), Items.RICE_SEED, state -> state.isIn(net.minecraft.block.Blocks.DIRT)));
        public static final RegistryObject<Block> CUCUMBER_CROP = block("cucumber_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.CUCUMBER_SEED, state -> state.isIn(net.minecraft.block.Blocks.FARMLAND)));
        public static final RegistryObject<Block> SOY_CROP = block("soy_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.SOY_SEED, state -> state.isIn(net.minecraft.block.Blocks.FARMLAND)));
        public static final RegistryObject<Block> HORSERADISH_CROP = block("horseradish_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.HORSERADISH_SEED, state -> state.isIn(net.minecraft.block.Blocks.FARMLAND)));

    }

    public static class Items {

        public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<BlockItem> RICE_SEED = blockItem("rice_seed", Blocks.RICE_CROP);
        public static final RegistryObject<BlockItem> CUCUMBER_SEED = blockItem("cucumber_seed", Blocks.CUCUMBER_CROP);
        public static final RegistryObject<BlockItem> SOY_SEED = blockItem("soy_seed", Blocks.SOY_CROP);
        public static final RegistryObject<BlockItem> HORSERADISH_SEED = blockItem("horseradish_seed", Blocks.HORSERADISH_CROP);

        public static final RegistryObject<Item> RAW_TUNA = basicItem("raw_tuna");
        public static final RegistryObject<Item> RAW_TUNA_FILLET = amountItem("raw_tuna_fillet", 1000, 3000, 6000);
        public static final RegistryObject<Item> RAW_SALMON_FILLET = amountItem("raw_salmon_fillet", 500, 2000, 4000);
    }
}
