package com.buuz135.sushigocrafting.world.tree;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class AvocadoTreeTrunkPlacer extends StraightTrunkPlacer {

    public AvocadoTreeTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    protected static boolean placeWithLeaves(LevelSimulatedRW reader, Random rand, BlockPos pos,TreeConfiguration config) {
        if (TreeFeature.validTreePos(reader, pos)) {

            placeLog(reader, (pos1, state) -> reader.setBlock(pos1, SushiContent.Blocks.AVOCADO_LEAVES_LOG.get().defaultBlockState(), 19),rand, pos, config);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> states, Random rand, int treeHeight, BlockPos pos, TreeConfiguration config) {

        for (int i = 0; i < treeHeight - 3; ++i) {
            placeLog(reader,states, rand, pos.above(i), config);
        }
        for (int i = treeHeight - 3; i < treeHeight - 1; ++i) {
            if (reader instanceof LevelSimulatedRW) placeWithLeaves((LevelSimulatedRW) reader, rand, pos.above(i), config);
        }
        if (reader instanceof LevelSimulatedRW) placeLog(reader, (pos1, state) -> ((LevelSimulatedRW)reader).setBlock(pos1, SushiContent.Blocks.AVOCADO_LEAVES.get().defaultBlockState(), 19),rand, pos.above(treeHeight - 1), config);

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(treeHeight), 0, false));
    }



}
