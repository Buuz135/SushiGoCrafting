package com.buuz135.sushigocrafting.proxy;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.machinery.*;
import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.block.plant.AvocadoLogBlock;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.block.plant.WaterCropBlock;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
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
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class SushiContent {

    public static <T extends Block> DeferredHolder<Block, T> block(String id, Supplier<T> block) {
        return Blocks.REGISTRY.register(id, block);
    }

    public static DeferredHolder<Item, Item> item(String id, Supplier<Item> item) {
        return Items.REGISTRY.register(id, () -> {
            var i = item.get();
            //SushiGoCrafting.TAB.getTabList().add(i);
            return i;
        });
    }

    public static DeferredHolder<Item, Item> basicItem(String id, String category) {
        return Items.REGISTRY.register(id, () -> new SushiItem(new Item.Properties(), category));
    }

    public static DeferredHolder<Item, AmountItem> amountItem(String id, String category, int minAmount, int maxAmount, int maxCombine, boolean hurts) {
        return Items.REGISTRY.register(id, () -> {
            var item = new AmountItem(new Item.Properties().stacksTo(1), category, minAmount, maxAmount, maxCombine, hurts);
            //SushiGoCrafting.TAB.getTabList().add(item);
            return item;
        });
    }

    public static DeferredHolder<Item, BlockItem> blockItem(String id, Supplier<? extends Block> sup) {
        return Items.REGISTRY.register(id, () -> {
            var blockItem = new BlockItem(sup.get(), new Item.Properties());
            SushiGoCrafting.TAB.getTabList().add(blockItem);
            return blockItem;
        });
    }

    public static DeferredHolder<Item, BlockItem> blockItem(String id, Supplier<? extends Block> sup, Item.Properties properties) {
        return Items.REGISTRY.register(id, () -> {
            var blockItem = new BlockItem(sup.get(), properties);
            SushiGoCrafting.TAB.getTabList().add(blockItem);
            return blockItem;
        });
    }

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> tile(String id, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> sup) {
        return TileEntities.REGISTRY.register(id, () -> BlockEntityType.Builder.of(supplier, sup.get()).build(null));
    }

    public static DeferredHolder<MobEffect, MobEffect> effect(String id, Supplier<MobEffect> supplier) {
        return Effects.REGISTRY.register(id, supplier);
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> entity(String id, Supplier<EntityType<T>> supplier) {
        return EntityTypes.REGISTRY.register(id, supplier);
    }

    public static <T extends IGlobalLootModifier> DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> lootSerializer(String id, Supplier<MapCodec<T>> supplier) {
        return LootSerializers.REGISTRY.register(id, supplier);
    }

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> recipeSerializer(String id, Supplier<RecipeSerializer<?>> supplier) {
        return RecipeSerializers.REGISTRY.register(id, supplier);
    }

    public static DeferredHolder<RecipeType<?>, RecipeType<?>> recipeType(String id, Supplier<RecipeType<?>> supplier) {
        return RecipeTypes.REGISTRY.register(id, supplier);
    }

    public static class Blocks {

        public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<Block, CustomCropBlock> RICE_CROP = block("rice_crop", () -> new WaterCropBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.KELP_PLANT), Items.RICE_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.DIRT)));
        public static final DeferredHolder<Block, CustomCropBlock> CUCUMBER_CROP = block("cucumber_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WHEAT), Items.CUCUMBER_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final DeferredHolder<Block, CustomCropBlock> SOY_CROP = block("soy_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WHEAT), Items.SOY_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final DeferredHolder<Block, CustomCropBlock> WASABI_CROP = block("wasabi_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WHEAT), Items.WASABI_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));
        public static final DeferredHolder<Block, CustomCropBlock> SESAME_CROP = block("sesame_crop", () -> new CustomCropBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.WHEAT), Items.SESAME_SEEDS, state -> state.is(net.minecraft.world.level.block.Blocks.FARMLAND)));

        public static final DeferredHolder<Block, RotatedPillarBlock> AVOCADO_LOG = block("avocado_log", () -> new AvocadoLogBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.OAK_WOOD)));
        public static final DeferredHolder<Block, RotatedPillarBlock> AVOCADO_LEAVES_LOG = block("avocado_leaves_logged", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.OAK_WOOD).noOcclusion()));
        public static final DeferredHolder<Block, Block> AVOCADO_LEAVES = block("avocado_leaves", AvocadoLeavesBlock::new);
        public static final DeferredHolder<Block, Block> AVOCADO_SAPLING = block("avocado_sapling", () -> new SaplingBlock(new TreeGrower(
                "avocado",
                Optional.empty(),
                Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("sushigocrafting", "avocado_tree"))),
                Optional.empty()
        ), BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.OAK_SAPLING)));

        public static final DeferredHolder<Block, RollerBlock> ROLLER = block("roller", RollerBlock::new);
        public static final DeferredHolder<Block, RiceCookerBlock> RICE_COOKER = block("rice_cooker", RiceCookerBlock::new);
        public static final DeferredHolder<Block, CuttingBoardBlock> CUTTING_BOARD = block("cutting_board", CuttingBoardBlock::new);
        public static final DeferredHolder<Block, CoolerBoxBlock> COOLER_BOX = block("cooler_box", CoolerBoxBlock::new);
        public static final DeferredHolder<Block, FermentationBarrelBlock> FERMENTATION_BARREL = block("fermentation_barrel", FermentationBarrelBlock::new);

    }

    public static class Items {

        public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<Item, BlockItem> RICE_SEEDS = blockItem("rice_seeds", Blocks.RICE_CROP);
        public static final DeferredHolder<Item, Item> RICE = basicItem("rice", "plant");

        public static final DeferredHolder<Item, BlockItem> CUCUMBER_SEEDS = blockItem("cucumber_seeds", Blocks.CUCUMBER_CROP);
        public static final DeferredHolder<Item, Item> CUCUMBER = basicItem("cucumber", "plant");

        public static final DeferredHolder<Item, BlockItem> SOY_SEEDS = blockItem("soy_seeds", Blocks.SOY_CROP);
        public static final DeferredHolder<Item, Item> SOY_BEAN = basicItem("soy_bean", "plant");

        public static final DeferredHolder<Item, BlockItem> WASABI_SEEDS = blockItem("wasabi_seeds", Blocks.WASABI_CROP);
        public static final DeferredHolder<Item, Item> WASABI_ROOT = basicItem("wasabi_root", "plant");

        public static final DeferredHolder<Item, BlockItem> SESAME_SEEDS = blockItem("sesame_seeds", Blocks.SESAME_CROP);

        public static final DeferredHolder<Item, Item> SEAWEED_ON_A_STICK = basicItem("seaweed_on_a_stick", "");

        public static final DeferredHolder<Item, Item> RAW_TUNA = basicItem("raw_tuna", "");

        public static final DeferredHolder<Item, Item> AVOCADO = basicItem("avocado", "plant");
        public static final DeferredHolder<Item, BlockItem> AVOCADO_LOG = blockItem("avocado_log", Blocks.AVOCADO_LOG);
        public static final DeferredHolder<Item, BlockItem> AVOCADO_LEAVES = blockItem("avocado_leaves", Blocks.AVOCADO_LEAVES);
        public static final DeferredHolder<Item, BlockItem> AVOCADO_LEAVES_LOG = blockItem("avocado_leaves_logged", Blocks.AVOCADO_LEAVES_LOG);
        public static final DeferredHolder<Item, BlockItem> AVOCADO_SAPLING = blockItem("avocado_sapling", Blocks.AVOCADO_SAPLING);

        public static final DeferredHolder<Item, BlockItem> ROLLER = blockItem("roller", Blocks.ROLLER);
        public static final DeferredHolder<Item, BlockItem> RICE_COOKER = blockItem("rice_cooker", Blocks.RICE_COOKER);
        public static final DeferredHolder<Item, BlockItem> CUTTING_BOARD = blockItem("cutting_board", Blocks.CUTTING_BOARD);
        public static final DeferredHolder<Item, BlockItem> COOLER_BOX = blockItem("cooler_box", Blocks.COOLER_BOX, new Item.Properties().stacksTo(1));
        public static final DeferredHolder<Item, BlockItem> FERMENTATION_BARREL = blockItem("fermentation_barrel", Blocks.FERMENTATION_BARREL);

        public static final DeferredHolder<Item, AmountItem> AVOCADO_SLICES = amountItem("avocado_slices", "ingredient", 100, 500, 1000, false);
        public static final DeferredHolder<Item, AmountItem> RAW_TUNA_FILLET = amountItem("tuna_fillet", "ingredient", 1000, 3000, 6000, false);
        public static final DeferredHolder<Item, AmountItem> RAW_SALMON_FILLET = amountItem("salmon_fillet", "ingredient", 500, 2000, 4000, false);
        public static final DeferredHolder<Item, Item> NORI_SHEET = basicItem("nori_sheets", "ingredient");
        public static final DeferredHolder<Item, AmountItem> COOKED_RICE = amountItem("cooked_rice", "ingredient", 50, 500, 2000, false);
        public static final DeferredHolder<Item, AmountItem> CUCUMBER_SLICES = amountItem("cucumber_slices", "ingredient", 50, 200, 400, false);
        public static final DeferredHolder<Item, AmountItem> IMITATION_CRAB = amountItem("imitation_crab", "ingredient", 50, 200, 400, false);
        public static final DeferredHolder<Item, AmountItem> SESAME_SEED = amountItem("sesame_seed", "ingredient", 10, 100, 200, false);
        public static final DeferredHolder<Item, AmountItem> TOBIKO = amountItem("tobiko", "ingredient", 10, 50, 100, false);
        public static final DeferredHolder<Item, AmountItem> CHEESE = amountItem("cheese", "ingredient", 50, 250, 500, false);
        public static final DeferredHolder<Item, AmountItem> SHRIMP = amountItem("shrimp", "ingredient", 20, 50, 100, false);
        public static final DeferredHolder<Item, AmountItem> SOY_SAUCE = amountItem("soy_sauce", "ingredient", 10, 50, 100, true);
        public static final DeferredHolder<Item, AmountItem> WASABI_PASTE = amountItem("wasabi_paste", "ingredient", 10, 50, 100, true);

        public static final DeferredHolder<Item, Item> KNIFE_CLEAVER = basicItem("cleaver_knife", "");

        public static final DeferredHolder<Item, Item> TUNA_BUCKET = item("tuna_bucket", () -> new MobBucketItem(EntityTypes.TUNA.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
        public static final DeferredHolder<Item, Item> SHRIMP_BUCKET = item("shrimp_bucket", () -> new MobBucketItem(EntityTypes.SHRIMP.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1)));

    }

    public static class TileEntities {

        public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SushiGoCrafting.MOD_ID);

        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RollerTile>> ROLLER = tile("roller", RollerTile::new, Blocks.ROLLER);
        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<RiceCookerTile>> RICE_COOKER = tile("rice_cooker", RiceCookerTile::new, Blocks.RICE_COOKER);
        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CuttingBoardTile>> CUTTING_BOARD = tile("cutting_board", CuttingBoardTile::new, Blocks.CUTTING_BOARD);
        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CoolerBoxTile>> COOLER_BOX = tile("cooler_box", CoolerBoxTile::new, Blocks.COOLER_BOX);
        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FermentationBarrelTile>> FERMENTATION_BARREL = tile("fermentation_barrel", FermentationBarrelTile::new, Blocks.FERMENTATION_BARREL);

    }

    public static class Effects {

        public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<MobEffect, MobEffect> ACQUIRED_TASTE = effect("acquired_taste", AcquiredTasteEffect::new);
        public static final DeferredHolder<MobEffect, MobEffect> SMALL_BITES = effect("small_bites", SmallBitesEffect::new);
        public static final DeferredHolder<MobEffect, MobEffect> STEADY_HANDS = effect("steady_hands", SteadyHandsEffect::new);

    }

    public static class EntityTypes {

        public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, SushiGoCrafting.MOD_ID);
        public static final DeferredHolder<EntityType<?>, EntityType<TunaEntity>> TUNA = entity("tuna", () -> EntityType.Builder.of(TunaEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4)./*setCustomClientFactory((spawnEntity, world) -> new TunaEntity(getTuna().get(), world)).*/build("tuna"));
        public static final DeferredHolder<EntityType<?>, EntityType<ShrimpEntity>> SHRIMP = entity("shrimp", () -> EntityType.Builder.of(ShrimpEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4)./*setCustomClientFactory((spawnEntity, world) -> new ShrimpEntity(getShrimp().get(), world)).*/build("shrimp"));

        public static DeferredHolder<EntityType<?>, EntityType<TunaEntity>> getTuna() {
            return TUNA;
        }

        public static DeferredHolder<EntityType<?>, EntityType<ShrimpEntity>> getShrimp() {
            return SHRIMP;
        }

    }

    public static class LootSerializers {

        public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<SeedsLootModifier>> SEEDS = lootSerializer("grass_seeds", () -> SeedsLootModifier.CODEC);
        public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ItemAmountLootModifier>> ITEM_AMOUNT = lootSerializer("item_amount", () -> ItemAmountLootModifier.CODEC);

    }

    public static class RecipeSerializers {

        public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COMBINE_AMOUNT = recipeSerializer("amount_combine_recipe", () -> new SimpleCraftingRecipeSerializer<>((craftingBookCategory) -> new CombineAmountItemRecipe()));
        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CUTTING_BOARD = recipeSerializer("cutting_board", () -> new GenericSerializer<>(CuttingBoardRecipe.class, RecipeTypes.CUTTING_BOARD, CuttingBoardRecipe.CODEC));
        public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> FERMENTING_BARREL = recipeSerializer("fermenting_barrel", () -> new GenericSerializer<>(FermentingBarrelRecipe.class, RecipeTypes.FERMENTING_BARREL, FermentingBarrelRecipe.CODEC));

    }

    public static class RecipeTypes {

        public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<RecipeType<?>, RecipeType<?>> CUTTING_BOARD = recipeType("cutting_board", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "cutting_board")));
        public static final DeferredHolder<RecipeType<?>, RecipeType<?>> FERMENTING_BARREL = recipeType("fermenting_barrel", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "fermenting_barrel")));

    }

    public static class AttachmentTypes {

        public static final DeferredRegister<AttachmentType<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SushiGoCrafting.MOD_ID);

        public static final DeferredHolder<AttachmentType<?>, AttachmentType<SushiWeightDiscoveryCapability>> SUSHI_WEIGHT_DISCOVERY = REGISTRY.register("sushi_weight_discovery", () -> AttachmentType.serializable(SushiWeightDiscoveryCapability::new)
                .copyOnDeath()
                .copyHandler(new IAttachmentCopyHandler<>() {
                    @Override
                    public @Nullable SushiWeightDiscoveryCapability copy(SushiWeightDiscoveryCapability sushiWeightDiscoveryCapability, IAttachmentHolder iAttachmentHolder, HolderLookup.Provider provider) {
                        return sushiWeightDiscoveryCapability;
                    }
                })
                .build());
    }
}
