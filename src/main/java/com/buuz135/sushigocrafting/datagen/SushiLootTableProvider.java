package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SushiLootTableProvider extends LootTableProvider {

    public SushiLootTableProvider(DataGenerator gen, CompletableFuture<HolderLookup.Provider> registries) {
        super(gen.getPackOutput(), new HashSet<>(), new ArrayList<>(), registries);
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return Arrays.asList(new SubProviderEntry(SushiBlockLootTables::new, LootContextParamSets.BLOCK), new SubProviderEntry(SushiEntityLootTables::new, LootContextParamSets.ENTITY));
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        //super.validate(writableregistry, validationcontext, problemreporter$collector);
    }

    private static class SushiBlockLootTables extends BlockLootSubProvider {

        private final List<Block> knownBlocks = new ArrayList<>();

        protected SushiBlockLootTables(HolderLookup.Provider registries) {
            super(new HashSet<>(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            this.crop(SushiContent.Blocks.RICE_CROP, SushiContent.Items.RICE);
            this.crop(SushiContent.Blocks.CUCUMBER_CROP, SushiContent.Items.CUCUMBER);
            this.crop(SushiContent.Blocks.SOY_CROP, SushiContent.Items.SOY_BEAN);
            this.crop(SushiContent.Blocks.WASABI_CROP, SushiContent.Items.WASABI_ROOT);
            this.crop(SushiContent.Blocks.SESAME_CROP, SushiContent.Items.SESAME_SEED);
            //this.dropOther(SushiContent.Blocks.SEAWEED, SushiContent.Items.SEAWEED);
            //this.dropOther(SushiContent.Blocks.SEAWEED_PLANT, SushiContent.Items.SEAWEED);
            this.dropSelf(SushiContent.Blocks.AVOCADO_SAPLING);
            this.dropSelf(SushiContent.Blocks.RICE_COOKER);
            //this.dropSelf(SushiContent.Blocks.SEAWEED_BLOCK);
            this.dropSelf(SushiContent.Blocks.CUTTING_BOARD);
            this.dropSelf(SushiContent.Blocks.AVOCADO_LOG);
            this.dropSelf(SushiContent.Blocks.ROLLER);
            this.dropSelf(SushiContent.Blocks.FERMENTATION_BARREL);
            this.dropLeaves(SushiContent.Blocks.AVOCADO_LEAVES, SushiContent.Blocks.AVOCADO_SAPLING);
            this.dropLeavesSpecial(SushiContent.Blocks.AVOCADO_LEAVES_LOG, SushiContent.Blocks.AVOCADO_LOG);
            this.add(SushiContent.Blocks.COOLER_BOX.get(), noDrop());
            knownBlocks.add(SushiContent.Blocks.COOLER_BOX.get());
        }

        private void crop(Supplier<CustomCropBlock> blockSupplier, Supplier<? extends Item> extra) {
            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockSupplier.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(blockSupplier.get().getAgeProperty(), blockSupplier.get().getMaxAge()));
            this.add(blockSupplier.get(), createCropDrops(blockSupplier.get(), extra.get(), blockSupplier.get().getBaseSeedId().asItem(), condition));
            knownBlocks.add(blockSupplier.get());
        }

        private void dropSelf(Supplier<? extends Block> blockSupplier) {
            this.dropSelf(blockSupplier.get());
            knownBlocks.add(blockSupplier.get());
        }

        private void dropOther(Supplier<Block> blockSupplier, Supplier<? extends ItemLike> other) {
            this.dropOther(blockSupplier.get(), other.get());
            knownBlocks.add(blockSupplier.get());
        }

        private void dropLeaves(Supplier<Block> blockSupplier, Supplier<Block> sapling) {
            this.add(blockSupplier.get(), (leaves) -> createLeavesDrops(leaves, sapling.get(), 0.20F, 0.25F, 0.35F, 0.45F));
            knownBlocks.add(blockSupplier.get());
        }

        private void dropLeavesSpecial(Supplier<? extends Block> blockSupplier, Supplier<? extends Block> extra) {
            this.add(blockSupplier.get(), (block) -> {
                return createSilkTouchOrShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(extra.get())));
            });
            knownBlocks.add(blockSupplier.get());
        }

        public void droppingSelfWithNbt(Supplier<? extends Block> itemProvider, CopyComponentsFunction.Builder nbtBuilder) {
            this.add(itemProvider.get(), LootTable.lootTable().withPool(applyExplosionCondition(itemProvider.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(itemProvider.get()).apply(nbtBuilder)))));
            knownBlocks.add(itemProvider.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }
    }

    private class SushiEntityLootTables extends EntityLootSubProvider {

        protected SushiEntityLootTables(HolderLookup.Provider registries) {
            super(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        public void generate() {
            this.add(SushiContent.EntityTypes.TUNA.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(SushiContent.Items.RAW_TUNA.get()))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemRandomChanceCondition.randomChance(0.05F))));
            this.add(SushiContent.EntityTypes.SHRIMP.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(SushiContent.Items.SHRIMP.get()))));
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return Arrays.asList(SushiContent.EntityTypes.TUNA.get(), (EntityType<?>) SushiContent.EntityTypes.SHRIMP.get()).stream();
        }

    }
}
