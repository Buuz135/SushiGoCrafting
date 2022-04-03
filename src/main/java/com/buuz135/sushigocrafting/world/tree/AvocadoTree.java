package com.buuz135.sushigocrafting.world.tree;

import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.Nullable;
import java.util.Random;

public class AvocadoTree extends AbstractTreeGrower {

    public static TreeConfiguration TREE = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(SushiContent.Blocks.AVOCADO_LOG.get().defaultBlockState()),
            new AvocadoTreeTrunkPlacer(4, 1, 0),
            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState(), 6)
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState().setValue(AvocadoLeavesBlock.STAGE, 1), 3)
                    .add(SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState().setValue(AvocadoLeavesBlock.STAGE, 2), 1)),
            new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build();

    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> HOLDER = FeatureUtils.register("avocado_tree", Feature.TREE, AvocadoTree.TREE);
    public static Holder<PlacedFeature> PLACEMENT = PlacementUtils.register("avocado_tree", AvocadoTree.HOLDER, PlacementUtils.countExtra(0, 0.05F, 1), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(0), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome());

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<TreeConfiguration, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return HOLDER;
    }
}
