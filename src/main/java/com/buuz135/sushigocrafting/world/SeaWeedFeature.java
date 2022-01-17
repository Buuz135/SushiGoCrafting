package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class SeaWeedFeature extends Feature<NoneFeatureConfiguration> {

    public SeaWeedFeature(Codec<NoneFeatureConfiguration> p_i231953_1_) {
        super(p_i231953_1_);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin(), context.config());
    }

    public boolean place(WorldGenLevel p_230362_1_, ChunkGenerator p_230362_3_, Random p_230362_4_, BlockPos p_230362_5_, NoneFeatureConfiguration p_230362_6_) {
        int i = 0;
        int j = p_230362_1_.getHeight(Heightmap.Types.OCEAN_FLOOR, p_230362_5_.getX(), p_230362_5_.getZ());
        BlockPos blockpos = new BlockPos(p_230362_5_.getX(), j, p_230362_5_.getZ());
        if (p_230362_1_.getBlockState(blockpos).is(Blocks.WATER)) {
            BlockState blockstate = SushiContent.Blocks.SEAWEED.get().defaultBlockState();
            BlockState blockstate1 = SushiContent.Blocks.SEAWEED_PLANT.get().defaultBlockState();
            int k = 1 + p_230362_4_.nextInt(10);

            for (int l = 0; l <= k; ++l) {
                if (p_230362_1_.getBlockState(blockpos).is(Blocks.WATER) && p_230362_1_.getBlockState(blockpos.above()).is(Blocks.WATER) && blockstate1.canSurvive(p_230362_1_, blockpos)) {
                    if (l == k) {
                        p_230362_1_.setBlock(blockpos, blockstate.setValue(KelpBlock.AGE, Integer.valueOf(p_230362_4_.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        p_230362_1_.setBlock(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.below();
                    if (blockstate.canSurvive(p_230362_1_, blockpos1) && !p_230362_1_.getBlockState(blockpos1.below()).is(Blocks.KELP)) {
                        p_230362_1_.setBlock(blockpos1, blockstate.setValue(KelpBlock.AGE, Integer.valueOf(p_230362_4_.nextInt(4) + 20)), 2);
                        ++i;
                    }
                    break;
                }

                blockpos = blockpos.above();
            }
        }

        return i > 0;
    }
}
