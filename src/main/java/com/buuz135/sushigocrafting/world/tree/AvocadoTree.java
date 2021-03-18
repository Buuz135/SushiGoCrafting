package com.buuz135.sushigocrafting.world.tree;

import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class AvocadoTree extends Tree {

    public static BaseTreeFeatureConfig TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(SushiContent.Blocks.AVOCADO_LOG.get().getDefaultState()),
            new WeightedBlockStateProvider()
                    .addWeightedBlockstate(SushiContent.Blocks.AVOCADO_LEAVES.get().getDefaultState(), 6)
                    .addWeightedBlockstate(SushiContent.Blocks.AVOCADO_LEAVES.get().getDefaultState().with(AvocadoLeavesBlock.STAGE, 1), 3)
                    .addWeightedBlockstate(SushiContent.Blocks.AVOCADO_LEAVES.get().getDefaultState().with(AvocadoLeavesBlock.STAGE, 2), 1),
            new BlobFoliagePlacer(FeatureSpread.create(1), FeatureSpread.create(0), 3),
            new AvocadoTreeTrunkPlacer(4, 1, 0), new TwoLayerFeature(1, 0, 1)).setIgnoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.withConfiguration(TREE);
    }
}
