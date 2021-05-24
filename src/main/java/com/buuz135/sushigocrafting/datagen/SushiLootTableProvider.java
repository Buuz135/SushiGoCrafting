package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SushiLootTableProvider extends LootTableProvider {

    public SushiLootTableProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return Arrays.asList(Pair.of(SushiBlockLootTables::new, LootParameterSets.BLOCK), Pair.of(SushiEntityLootTables::new, LootParameterSets.ENTITY));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        //super.validate(map, validationtracker);
    }

    private static class SushiBlockLootTables extends BlockLootTables {

        private List<Block> knownBlocks = new ArrayList<>();

        @Override
        protected void addTables() {
            this.crop(SushiContent.Blocks.RICE_CROP, SushiContent.Items.RICE);
            this.crop(SushiContent.Blocks.CUCUMBER_CROP, SushiContent.Items.CUCUMBER);
            this.crop(SushiContent.Blocks.SOY_CROP, SushiContent.Items.SOY_BEAN);
            this.crop(SushiContent.Blocks.WASABI_CROP, SushiContent.Items.WASABI_ROOT);
            this.crop(SushiContent.Blocks.SESAME_CROP, SushiContent.Items.SESAME_SEED);
            this.dropOther(SushiContent.Blocks.SEAWEED, SushiContent.Items.SEAWEED);
            this.dropOther(SushiContent.Blocks.SEAWEED_PLANT, SushiContent.Items.SEAWEED);
            this.dropSelf(SushiContent.Blocks.AVOCADO_SAPLING);
            this.dropSelf(SushiContent.Blocks.RICE_COOKER);
            this.dropSelf(SushiContent.Blocks.SEAWEED_BLOCK);
            this.dropSelf(SushiContent.Blocks.CUTTING_BOARD);
            this.dropSelf(SushiContent.Blocks.AVOCADO_LOG);
            this.dropSelf(SushiContent.Blocks.ROLLER);
            this.dropSelf(SushiContent.Blocks.COOLER_BOX);
            this.dropLeaves(SushiContent.Blocks.AVOCADO_LEAVES, SushiContent.Blocks.AVOCADO_SAPLING);
            this.dropLeavesSpecial(SushiContent.Blocks.AVOCADO_LEAVES_LOG, SushiContent.Blocks.AVOCADO_LOG);
        }

        private void crop(Supplier<CustomCropBlock> blockSupplier, Supplier<? extends Item> extra) {
            ILootCondition.IBuilder condition = BlockStateProperty.builder(blockSupplier.get()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(blockSupplier.get().getAgeProperty(), blockSupplier.get().getMaxAge()));
            this.registerLootTable(blockSupplier.get(), droppingAndBonusWhen(blockSupplier.get(), extra.get(), blockSupplier.get().getSeedsItem().asItem(), condition));
            knownBlocks.add(blockSupplier.get());
        }

        private void dropSelf(Supplier<? extends Block> blockSupplier) {
            this.registerDropSelfLootTable(blockSupplier.get());
            knownBlocks.add(blockSupplier.get());
        }

        private void dropOther(Supplier<Block> blockSupplier, Supplier<? extends IItemProvider> other) {
            this.registerDropping(blockSupplier.get(), other.get());
            knownBlocks.add(blockSupplier.get());
        }

        private void dropLeaves(Supplier<Block> blockSupplier, Supplier<Block> sapling) {
            this.registerLootTable(blockSupplier.get(), (leaves) -> droppingWithChancesAndSticks(leaves, sapling.get(), 0.15F, 0.2F, 0.3F, 0.4F));
            knownBlocks.add(blockSupplier.get());
        }

        private void dropLeavesSpecial(Supplier<? extends Block> blockSupplier, Supplier<? extends Block> extra) {
            this.registerLootTable(blockSupplier.get(), (block) -> {
                return droppingWithSilkTouchOrShears(block, withSurvivesExplosion(block, ItemLootEntry.builder(extra.get())));
            });
            knownBlocks.add(blockSupplier.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }
    }

    private class SushiEntityLootTables extends EntityLootTables {

        @Override
        protected void addTables() {
            this.registerLootTable(SushiContent.EntityTypes.TUNA.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(SushiContent.Items.RAW_TUNA.get()))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.BONE_MEAL)).acceptCondition(RandomChance.builder(0.05F))));
            this.registerLootTable(SushiContent.EntityTypes.SHRIMP.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(SushiContent.Items.SHRIMP.get()))));
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return Arrays.asList(SushiContent.EntityTypes.TUNA.get(), SushiContent.EntityTypes.SHRIMP.get());
        }
    }
}
