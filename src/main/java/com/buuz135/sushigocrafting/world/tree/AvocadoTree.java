package com.buuz135.sushigocrafting.world.tree;

import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import javax.annotation.Nullable;
import java.util.Random;

public class AvocadoTree extends AbstractTreeGrower {

    public static TreeConfiguration TREE = new TreeConfiguration.TreeConfigurationBuilder(
            SimpleStateProvider.simple(SushiContent.Blocks.AVOCADO_LOG.get().defaultBlockState()),
            new AvocadoTreeTrunkPlacer(4, 1, 0),
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState(), 6)
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState().setValue(AvocadoLeavesBlock.STAGE, 1), 3)
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState().setValue(AvocadoLeavesBlock.STAGE, 2), 1)),
            new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.configured(TREE);
    }
}
