package com.buuz135.sushigocrafting.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

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
        return Arrays.asList(Pair.of(SushiBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        //super.validate(map, validationtracker);
    }

    private static class SushiBlockLootTables extends BlockLootTables {

        public static LootTable.Builder dropping(Block block, ILootCondition.IBuilder conditionBuilder, LootEntry.Builder<?> p_218494_2_) {
            return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptCondition(conditionBuilder).alternatively(p_218494_2_)));
        }

        protected static LootTable.Builder droppingWithShears(Block block, LootEntry.Builder<?> noShearAlternativeEntry) {
            return dropping(block, MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)), noShearAlternativeEntry);
        }

        public static LootTable.Builder droppingSeeds(Block block) {
            return droppingWithShears(block,
                    withExplosionDecay(block, ItemLootEntry.builder(Items.WHEAT_SEEDS).acceptCondition(RandomChance.builder(0.125F)).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 2))));
        }

        @Override
        protected void addTables() {
            //droppingSeeds(Blocks.GRASS);
            //this.registerLootTable(Blocks.GRASS, LootTable.builder().addLootPool(withExplosionDecay(Blocks.GRASS, LootPool.builder().rolls(ConstantRange.of(1)).addEntry(seedDrop(SushiContent.Items.RICE_SEEDS.get())))));
            ;
        }

        public LootTable.Builder droppingSelf(IItemProvider itemProvider) {
            return LootTable.builder()
                    .addLootPool(withSurvivesExplosion(itemProvider, LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(itemProvider))));
        }

        private LootEntry.Builder<?> seedDrop(IItemProvider item) {
            return ItemLootEntry.builder(item).acceptCondition(RandomChance.builder(0.125F)).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE, 2));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Arrays.asList();
        }
    }

    private class SushiEntityLootTables extends EntityLootTables {

        @Override
        protected void addTables() {

        }


    }
}
