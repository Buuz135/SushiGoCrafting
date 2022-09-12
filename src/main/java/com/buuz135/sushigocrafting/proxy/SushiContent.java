package com.buuz135.sushigocrafting.proxy;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.SushiGoCraftingBlock;
import com.buuz135.sushigocrafting.block.machinery.*;
import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.block.plant.AvocadoLogBlock;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.block.plant.WaterCropBlock;
import com.buuz135.sushigocrafting.block.seaweed.SeaWeedBlock;
import com.buuz135.sushigocrafting.block.seaweed.SeaWeedTopBlock;
import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.buuz135.sushigocrafting.entity.TunaEntity;
import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.loot.ItemAmountLootModifier;
import com.buuz135.sushigocrafting.loot.SeedsLootModifier;
import com.buuz135.sushigocrafting.potioneffect.AcquiredTasteEffect;
import com.buuz135.sushigocrafting.potioneffect.SmallBitesEffect;
import com.buuz135.sushigocrafting.potioneffect.SteadyHandsEffect;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.buuz135.sushigocrafting.tile.machinery.*;
import com.buuz135.sushigocrafting.world.AvocadoTreeGrower;
import com.buuz135.sushigocrafting.world.SeaweedFeature;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SushiContent {

    public static <T extends Block> RegistryObject<T> block(String id, Supplier<T> block) {
        return Blocks.REGISTRY.register(id, block);
    }

    public static RegistryObject<Item> item(String id, Supplier<Item> item) {
        return Items.REGISTRY.register(id, item);
    }

    public static RegistryObject<Item> basicItem(String id, String category) {
        return Items.REGISTRY.register(id, () -> new SushiItem(new Item.Properties().tab(SushiGoCrafting.TAB), category));
    }

    public static RegistryObject<AmountItem> amountItem(String id, String category, int minAmount, int maxAmount, int maxCombine, boolean hurts) {
        return Items.REGISTRY.register(id, () -> new AmountItem(new Item.Properties().tab(SushiGoCrafting.TAB).stacksTo(1), category, minAmount, maxAmount, maxCombine, hurts));
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return Items.REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(SushiGoCrafting.TAB)));
    }

    public static RegistryObject<BlockItem> blockItem(String id, Supplier<? extends Block> sup, Item.Properties properties) {
        return Items.REGISTRY.register(id, () -> new BlockItem(sup.get(), properties.tab(SushiGoCrafting.TAB)));
    }

    public static <T extends FeatureConfiguration> RegistryObject<Feature<T>> feature(String id, Supplier<Feature<T>> featureSupplier) {
        return Features.REGISTRY.register(id, featureSupplier);
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> tile(String id, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> sup) {
        return TileEntities.REGISTRY.register(id, () -> BlockEntityType.Builder.of(supplier, sup.get()).build(null));
    }

    public static RegistryObject<MobEffect> effect(String id, Supplier<MobEffect> supplier) {
        return Effects.REGISTRY.register(id, supplier);
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> entity(String id, Supplier<EntityType<T>> supplier) {
        return EntityTypes.REGISTRY.register(id, supplier);
    }

    public static <T extends IGlobalLootModifier> RegistryObject<Codec<? extends IGlobalLootModifier>> lootSerializer(String id, Supplier<Codec<T>> supplier) {
        return LootSerializers.REGISTRY.register(id, supplier);
    }

    public static RegistryObject<RecipeSerializer> recipeSerializer(String id, Supplier<RecipeSerializer<?>> supplier) {
        return RecipeSerializers.REGISTRY.register(id, supplier);
    }

    public static RegistryObject<RecipeType<?>> recipeType(String id, Supplier<RecipeType<?>> supplier) {
        return RecipeTypes.REGISTRY.register(id, supplier);
    }

    public static class Blocks {

        public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<CustomCropBlock> RICE_CROP = block("rice_crop", () -> new WaterCropBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.KELP_PLANT), Items.RICE_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.DIRT)));
        public static final RegistryObject<CustomCropBlock> CUCUMBER_CROP = block("cucumber_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT), Items.CUCUMBER_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> SOY_CROP = block("soy_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT), Items.SOY_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> WASABI_CROP = block("wasabi_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT), Items.WASABI_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final RegistryObject<CustomCropBlock> SESAME_CROP = block("sesame_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT), Items.SESAME_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));

        public static final RegistryObject<Block> SEAWEED = block("seaweed", () -> new SeaWeedTopBlock(BlockBehaviour.Properties.of(Material.WATER_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.WET_GRASS)));
        public static final RegistryObject<Block> SEAWEED_PLANT = block("seaweed_plant", () -> new SeaWeedBlock(BlockBehaviour.Properties.of(Material.WATER_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.WET_GRASS)));
        public static final RegistryObject<Block> SEAWEED_BLOCK = block("dried_seaweed_block", () -> new SushiGoCraftingBlock("dried_seaweed_block", BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.DRIED_KELP_BLOCK)) {
            @Override
            public PushReaction getPistonPushReaction(BlockState state) {
                return PushReaction.DESTROY;
            }
        });

        public static final RegistryObject<RotatedPillarBlock> AVOCADO_LOG = block("avocado_log", () -> new AvocadoLogBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_WOOD)));
        public static final RegistryObject<RotatedPillarBlock> AVOCADO_LEAVES_LOG = block("avocado_leaves_logged", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_WOOD).noOcclusion()));
        public static final RegistryObject<Block> AVOCADO_LEAVES = block("avocado_leaves", AvocadoLeavesBlock::new);
        public static final RegistryObject<Block> AVOCADO_SAPLING = block("avocado_sapling", () -> new SaplingBlock(new AvocadoTreeGrower(), BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_SAPLING)));

        public static final RegistryObject<RollerBlock> ROLLER = block("roller", RollerBlock::new);
        public static final RegistryObject<RiceCookerBlock> RICE_COOKER = block("rice_cooker", RiceCookerBlock::new);
        public static final RegistryObject<CuttingBoardBlock> CUTTING_BOARD = block("cutting_board", CuttingBoardBlock::new);
        public static final RegistryObject<CoolerBoxBlock> COOLER_BOX = block("cooler_box", CoolerBoxBlock::new);
        public static final RegistryObject<FermentationBarrelBlock> FERMENTATION_BARREL = block("fermentation_barrel", FermentationBarrelBlock::new);

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
        public static final RegistryObject<Item> SEAWEED_ON_A_STICK = basicItem("seaweed_on_a_stick", "");

        public static final RegistryObject<Item> RAW_TUNA = basicItem("raw_tuna", "");

        public static final RegistryObject<Item> AVOCADO = basicItem("avocado", "plant");
        public static final RegistryObject<BlockItem> AVOCADO_LOG = blockItem("avocado_log", Blocks.AVOCADO_LOG);
        public static final RegistryObject<BlockItem> AVOCADO_LEAVES = blockItem("avocado_leaves", Blocks.AVOCADO_LEAVES);
        public static final RegistryObject<BlockItem> AVOCADO_LEAVES_LOG = blockItem("avocado_leaves_logged", Blocks.AVOCADO_LEAVES_LOG);
        public static final RegistryObject<BlockItem> AVOCADO_SAPLING = blockItem("avocado_sapling", Blocks.AVOCADO_SAPLING);

        public static final RegistryObject<BlockItem> ROLLER = blockItem("roller", Blocks.ROLLER);
        public static final RegistryObject<BlockItem> RICE_COOKER = blockItem("rice_cooker", Blocks.RICE_COOKER);
        public static final RegistryObject<BlockItem> CUTTING_BOARD = blockItem("cutting_board", Blocks.CUTTING_BOARD);
        public static final RegistryObject<BlockItem> COOLER_BOX = blockItem("cooler_box", Blocks.COOLER_BOX, new Item.Properties().stacksTo(1));
        public static final RegistryObject<BlockItem> FERMENTATION_BARREL = blockItem("fermentation_barrel", Blocks.FERMENTATION_BARREL);

        public static final RegistryObject<AmountItem> AVOCADO_SLICES = amountItem("avocado_slices", "ingredient", 100, 500, 1000, false);
        public static final RegistryObject<AmountItem> RAW_TUNA_FILLET = amountItem("tuna_fillet", "ingredient", 1000, 3000, 6000, false);
        public static final RegistryObject<AmountItem> RAW_SALMON_FILLET = amountItem("salmon_fillet", "ingredient", 500, 2000, 4000, false);
        public static final RegistryObject<Item> NORI_SHEET = basicItem("nori_sheets", "ingredient");
        public static final RegistryObject<AmountItem> COOKED_RICE = amountItem("cooked_rice", "ingredient", 50, 500, 2000, false);
        public static final RegistryObject<AmountItem> CUCUMBER_SLICES = amountItem("cucumber_slices", "ingredient", 50, 200, 400, false);
        public static final RegistryObject<AmountItem> IMITATION_CRAB = amountItem("imitation_crab", "ingredient", 50, 200, 400, false);
        public static final RegistryObject<AmountItem> SESAME_SEED = amountItem("sesame_seed", "ingredient", 10, 100, 200, false);
        public static final RegistryObject<AmountItem> TOBIKO = amountItem("tobiko", "ingredient", 10, 50, 100, false);
        public static final RegistryObject<AmountItem> CHEESE = amountItem("cheese", "ingredient", 50, 250, 500, false);
        public static final RegistryObject<AmountItem> SHRIMP = amountItem("shrimp", "ingredient", 20, 50, 100, false);
        public static final RegistryObject<AmountItem> SOY_SAUCE = amountItem("soy_sauce", "ingredient", 10, 50, 100, true);
        public static final RegistryObject<AmountItem> WASABI_PASTE = amountItem("wasabi_paste", "ingredient", 10, 50, 100, true);

        public static final RegistryObject<Item> KNIFE_CLEAVER = basicItem("cleaver_knife", "");

        public static final RegistryObject<Item> TUNA_BUCKET = item("tuna_bucket", () -> new MobBucketItem(EntityTypes.TUNA, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).tab(SushiGoCrafting.TAB)));
        public static final RegistryObject<Item> SHRIMP_BUCKET = item("shrimp_bucket", () -> new MobBucketItem(EntityTypes.SHRIMP, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1).tab(SushiGoCrafting.TAB)));

    }

    public static class TileEntities {

        public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SushiGoCrafting.MOD_ID);

        public static RegistryObject<BlockEntityType<RollerTile>> ROLLER = tile("roller", RollerTile::new, Blocks.ROLLER);
        public static RegistryObject<BlockEntityType<RiceCookerTile>> RICE_COOKER = tile("rice_cooker", RiceCookerTile::new, Blocks.RICE_COOKER);
        public static RegistryObject<BlockEntityType<CuttingBoardTile>> CUTTING_BOARD = tile("cutting_board", CuttingBoardTile::new, Blocks.CUTTING_BOARD);
        public static RegistryObject<BlockEntityType<CoolerBoxTile>> COOLER_BOX = tile("cooler_box", CoolerBoxTile::new, Blocks.COOLER_BOX);
        public static RegistryObject<BlockEntityType<FermentationBarrelTile>> FERMENTATION_BARREL = tile("fermentation_barrel", FermentationBarrelTile::new, Blocks.FERMENTATION_BARREL);

    }


    public static class Features {

        public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<Feature<NoneFeatureConfiguration>> SEAWEED = feature("seaweed", () -> new SeaweedFeature(NoneFeatureConfiguration.CODEC));



    }

    public static class Effects {

        public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<MobEffect> ACQUIRED_TASTE = effect("acquired_taste", AcquiredTasteEffect::new);
        public static final RegistryObject<MobEffect> SMALL_BITES = effect("small_bites", SmallBitesEffect::new);
        public static final RegistryObject<MobEffect> STEADY_HANDS = effect("steady_hands", SteadyHandsEffect::new);

    }

    public static class EntityTypes {

        public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SushiGoCrafting.MOD_ID);

        public static RegistryObject<EntityType<TunaEntity>> getTuna() {
            return TUNA;
        }        public static final RegistryObject<EntityType<TunaEntity>> TUNA = entity("tuna", () -> EntityType.Builder.of(TunaEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4).setCustomClientFactory((spawnEntity, world) -> new TunaEntity(getTuna().get(), world)).build("tuna"));

        public static RegistryObject<EntityType<ShrimpEntity>> getShrimp() {
            return SHRIMP;
        }        public static final RegistryObject<EntityType<ShrimpEntity>> SHRIMP = entity("shrimp", () -> EntityType.Builder.of(ShrimpEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4).setCustomClientFactory((spawnEntity, world) -> new ShrimpEntity(getShrimp().get(), world)).build("shrimp"));

    }

    public static class LootSerializers {

        public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<Codec<? extends IGlobalLootModifier>> SEEDS = lootSerializer("grass_seeds", SeedsLootModifier.CODEC);
        public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ITEM_AMOUNT = lootSerializer("item_amount", ItemAmountLootModifier.CODEC);

    }

    public static class RecipeSerializers {

        public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<RecipeSerializer> COMBINE_AMOUNT = recipeSerializer("amount_combine_recipe", () -> new SimpleRecipeSerializer<>(CombineAmountItemRecipe::new));
        public static final RegistryObject<RecipeSerializer> CUTTING_BOARD = recipeSerializer("cutting_board", () -> new GenericSerializer<>(CuttingBoardRecipe.class, RecipeTypes.CUTTING_BOARD));
        public static final RegistryObject<RecipeSerializer> FERMENTING_BARREL = recipeSerializer("fermenting_barrel", () -> new GenericSerializer<>(FermentingBarrelRecipe.class, RecipeTypes.FERMENTING_BARREL));

    }

    public static class RecipeTypes {

        public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_TYPES, SushiGoCrafting.MOD_ID);

        public static final RegistryObject<RecipeType<?>> CUTTING_BOARD = recipeType("cutting_board", () -> RecipeType.simple(new ResourceLocation(SushiGoCrafting.MOD_ID, "cutting_board")));
        public static final RegistryObject<RecipeType<?>> FERMENTING_BARREL = recipeType("fermenting_barrel", () -> RecipeType.simple(new ResourceLocation(SushiGoCrafting.MOD_ID, "fermenting_barrel")));

    }

}
