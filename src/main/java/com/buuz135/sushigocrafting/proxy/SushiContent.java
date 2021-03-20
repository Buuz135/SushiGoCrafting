package com.buuz135.sushigocrafting.proxy;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.SushiGoCraftingBlock;
import com.buuz135.sushigocrafting.block.machinery.RiceCookerBlock;
import com.buuz135.sushigocrafting.block.machinery.RollerBlock;
import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.block.plant.AvocadoLogBlock;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.block.plant.WaterCropBlock;
import com.buuz135.sushigocrafting.block.seaweed.SeaWeedBlock;
import com.buuz135.sushigocrafting.block.seaweed.SeaWeedTopBlock;
import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.potioneffect.AcquiredTasteEffect;
import com.buuz135.sushigocrafting.potioneffect.SmallBitesEffect;
import com.buuz135.sushigocrafting.potioneffect.SteadyHandsEffect;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.buuz135.sushigocrafting.world.SeaWeedFeature;
import com.buuz135.sushigocrafting.world.tree.AvocadoTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SushiContent {

    public static <T extends Block> RegistryObject<T> block(String id, Supplier<T> block) {
        return Blocks.REGISTRY.register(id, block);
    }

    public static RegistryObject<Item> basicItem(String id, String category) {
        return Items.REGISTRY.register(id, () -> new SushiItem(new Item.Properties().group(SushiGoCrafting.TAB), category));
    }

    public static RegistryObject<AmountItem> amountItem(String id, String category, int minAmount, int maxAmount, int maxCombine) {
        return Items.REGISTRY.register(id, () -> new AmountItem(new Item.Properties().group(SushiGoCrafting.TAB).maxStackSize(1), category, minAmount, maxAmount, maxCombine));
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return Items.REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().group(SushiGoCrafting.TAB)));
    }

    public static <T extends IFeatureConfig> RegistryObject<Feature<T>> feature(String id, Supplier<Feature<T>> featureSupplier) {
        return Features.REGISTRY.register(id, featureSupplier);
    }

    public static <T extends TileEntity> RegistryObject<TileEntityType<?>> tile(String id, Supplier<T> supplier, Supplier<? extends Block> sup) {
        return TileEntities.REGISTRY.register(id, () -> TileEntityType.Builder.create(supplier, sup.get()).build(null));
    }

    public static RegistryObject<Effect> effect(String id, Supplier<Effect> supplier) {
        return Effects.REGISTRY.register(id, supplier);
    }

    public static class Blocks {

        public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<CustomCropBlock> RICE_CROP = block("rice_crop", () -> new WaterCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.KELP_PLANT), Items.RICE_SEEDS, state -> state.matchesBlock(net.minecraft.block.Blocks.DIRT)));
        public static final RegistryObject<CustomCropBlock> CUCUMBER_CROP = block("cucumber_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.CUCUMBER_SEEDS, state -> state.matchesBlock(net.minecraft.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> SOY_CROP = block("soy_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.SOY_SEEDS, state -> state.matchesBlock(net.minecraft.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> WASABI_CROP = block("wasabi_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.WASABI_SEEDS, state -> state.matchesBlock(net.minecraft.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> SESAME_CROP = block("sesame_crop", () -> new CustomCropBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT), Items.SESAME_SEEDS, state -> state.matchesBlock(net.minecraft.block.Blocks.FARMLAND)));

        public static final RegistryObject<Block> SEAWEED = block("seaweed", () -> new SeaWeedTopBlock(AbstractBlock.Properties.create(Material.OCEAN_PLANT).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.WET_GRASS)));
        public static final RegistryObject<Block> SEAWEED_PLANT = block("seaweed_plant", () -> new SeaWeedBlock(AbstractBlock.Properties.create(Material.OCEAN_PLANT).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.WET_GRASS)));
        public static final RegistryObject<Block> SEAWEED_BLOCK = block("dried_seaweed_block", () -> new SushiGoCraftingBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.WHEAT)));

        public static final RegistryObject<RotatedPillarBlock> AVOCADO_LOG = block("avocado_log", () -> new AvocadoLogBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.OAK_WOOD)));
        public static final RegistryObject<RotatedPillarBlock> AVOCADO_LEAVES_LOG = block("avocado_leaves_logged", () -> new RotatedPillarBlock(AbstractBlock.Properties.from(net.minecraft.block.Blocks.OAK_WOOD).notSolid()));
        public static final RegistryObject<Block> AVOCADO_LEAVES = block("avocado_leaves", AvocadoLeavesBlock::new);
        public static final RegistryObject<Block> AVOCADO_SAPLING = block("avocado_sapling", () -> new SaplingBlock(new AvocadoTree(), AbstractBlock.Properties.from(net.minecraft.block.Blocks.OAK_SAPLING)));

        public static final RegistryObject<RollerBlock> ROLLER = block("roller", RollerBlock::new);
        public static final RegistryObject<RiceCookerBlock> RICE_COOKER = block("rice_cooker", RiceCookerBlock::new);

    }

    public static class Items {

        public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<BlockItem> RICE_SEEDS = blockItem("rice_seeds", Blocks.RICE_CROP);
        public static final RegistryObject<Item> RICE = basicItem("rice", "plant");

        public static final RegistryObject<BlockItem> CUCUMBER_SEEDS = blockItem("cucumber_seeds", Blocks.CUCUMBER_CROP);
        public static final RegistryObject<Item> CUCUMBER = basicItem("cucumber", "plant");

        public static final RegistryObject<BlockItem> SOY_SEEDS = blockItem("soy_seeds", Blocks.SOY_CROP);
        public static final RegistryObject<Item> SOY_BEAN = basicItem("soy_bean", "plant");

        public static final RegistryObject<BlockItem> WASABI_SEEDS = blockItem("wasabi_seeds", Blocks.WASABI_CROP);
        public static final RegistryObject<Item> WASABI_ROOT = basicItem("wasabi_root", "plant");

        public static final RegistryObject<BlockItem> SESAME_SEEDS = blockItem("sesame_seeds", Blocks.SESAME_CROP);

        public static final RegistryObject<BlockItem> SEAWEED = blockItem("seaweed", Blocks.SEAWEED);
        public static final RegistryObject<Item> DRY_SEAWEED = basicItem("dried_seaweed", "");
        public static final RegistryObject<BlockItem> SEAWEED_BLOCK = blockItem("dried_seaweed_block", Blocks.SEAWEED_BLOCK);

        public static final RegistryObject<Item> RAW_TUNA = basicItem("raw_tuna", "");
        public static final RegistryObject<Item> CRAB = basicItem("crab", "");

        public static final RegistryObject<Item> AVOCADO = basicItem("avocado", "plant");
        public static final RegistryObject<BlockItem> AVOCADO_LOG = blockItem("avocado_log", Blocks.AVOCADO_LOG);
        public static final RegistryObject<BlockItem> AVOCADO_LEAVES = blockItem("avocado_leaves", Blocks.AVOCADO_LEAVES);
        public static final RegistryObject<BlockItem> AVOCADO_LEAVES_LOG = blockItem("avocado_leaves_logged", Blocks.AVOCADO_LEAVES_LOG);
        public static final RegistryObject<BlockItem> AVOCADO_SAPLING = blockItem("avocado_sapling", Blocks.AVOCADO_SAPLING);

        public static final RegistryObject<BlockItem> ROLLER = blockItem("roller", Blocks.ROLLER);
        public static final RegistryObject<BlockItem> RICE_COOKER = blockItem("rice_cooker", Blocks.RICE_COOKER);

        public static final RegistryObject<AmountItem> AVOCADO_SLICES = amountItem("avocado_slices", "ingredient", 100, 500, 1000);
        public static final RegistryObject<AmountItem> RAW_TUNA_FILLET = amountItem("tuna_fillet", "ingredient", 1000, 3000, 6000);
        public static final RegistryObject<AmountItem> RAW_SALMON_FILLET = amountItem("salmon_fillet", "ingredient", 500, 2000, 4000);
        public static final RegistryObject<Item> NORI_SHEET = basicItem("nori_sheets", "ingredient");
        public static final RegistryObject<AmountItem> COOKED_RICE = amountItem("cooked_rice", "ingredient", 50, 500, 2000);
        public static final RegistryObject<AmountItem> CUCUMBER_SLICES = amountItem("cucumber_slices", "ingredient", 50, 200, 400);
        public static final RegistryObject<AmountItem> IMITATION_CRAB = amountItem("imitation_crab", "ingredient", 50, 200, 400);
        public static final RegistryObject<AmountItem> SESAME_SEED = amountItem("sesame_seed", "ingredient", 10, 100, 200);
        public static final RegistryObject<AmountItem> TOBIKO = amountItem("tobiko", "ingredient", 10, 50, 100);
        public static final RegistryObject<AmountItem> CHEESE = amountItem("cheese", "ingredient", 50, 250, 500);
        public static final RegistryObject<AmountItem> SHRIMP = amountItem("shrimp", "ingredient", 10, 50, 100);
        public static final RegistryObject<AmountItem> SOW_SAUCE = amountItem("soy_sauce", "ingredient", 10, 50, 100);
        public static final RegistryObject<AmountItem> WASABI_PASTE = amountItem("wasabi_paste", "ingredient", 10, 50, 100);

        public static final RegistryObject<Item> KNIFE_CLEAVER = basicItem("cleaver_knife", "");

    }

    public static class TileEntities {

        public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SushiGoCrafting.MOD_ID);

        public static RegistryObject<TileEntityType<?>> ROLLER = tile("roller", RollerTile::new, Blocks.ROLLER);
        public static RegistryObject<TileEntityType<?>> RICE_COOKER = tile("rice_cooker", RiceCookerTile::new, Blocks.RICE_COOKER);
    }


    public static class Features {

        public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<Feature<NoFeatureConfig>> SEAWEED = feature("seaweed", () -> new SeaWeedFeature(NoFeatureConfig.CODEC));

    }

    public static class Effects {

        public static final DeferredRegister<Effect> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<Effect> ACQUIRED_TASTE = effect("acquired_taste", AcquiredTasteEffect::new);
        public static final RegistryObject<Effect> SMALL_BITES = effect("small_bites", SmallBitesEffect::new);
        public static final RegistryObject<Effect> STEADY_HANDS = effect("steady_hands", SteadyHandsEffect::new);

    }

}
