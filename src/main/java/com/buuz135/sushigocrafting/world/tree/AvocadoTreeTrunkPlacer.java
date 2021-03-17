package com.buuz135.sushigocrafting.world.tree;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class AvocadoTreeTrunkPlacer extends StraightTrunkPlacer {

    public AvocadoTreeTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    protected static boolean placeWithLeaves(IWorldGenerationReader reader, Random rand, BlockPos pos, Set<BlockPos> p_236911_3_, MutableBoundingBox p_236911_4_, BaseTreeFeatureConfig config) {
        if (TreeFeature.isReplaceableAt(reader, pos)) {
            func_236913_a_(reader, pos, SushiContent.Blocks.AVOCADO_LEAVES_LOG.get().getDefaultState(), p_236911_4_);
            p_236911_3_.add(pos.toImmutable());
            return true;
        } else {
            return false;
        }
    }

    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader reader, Random rand, int treeHeight, BlockPos p_230382_4_, Set<BlockPos> p_230382_5_, MutableBoundingBox p_230382_6_, BaseTreeFeatureConfig p_230382_7_) {
        //func_236909_a_(reader, p_230382_4_.down());

        for (int i = 0; i < treeHeight - 3; ++i) {
            func_236911_a_(reader, rand, p_230382_4_.up(i), p_230382_5_, p_230382_6_, p_230382_7_);
        }
        for (int i = treeHeight - 3; i < treeHeight - 1; ++i) {
            placeWithLeaves(reader, rand, p_230382_4_.up(i), p_230382_5_, p_230382_6_, p_230382_7_);
        }
        func_236913_a_(reader, p_230382_4_.up(treeHeight - 1), SushiContent.Blocks.AVOCADO_LEAVES.get().getDefaultState(), p_230382_6_);
        return ImmutableList.of(new FoliagePlacer.Foliage(p_230382_4_.up(treeHeight), 0, false));
    }


}
