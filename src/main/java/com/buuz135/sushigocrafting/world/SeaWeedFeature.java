package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpTopBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class SeaWeedFeature extends Feature<NoFeatureConfig> {

    public SeaWeedFeature(Codec<NoFeatureConfig> p_i231953_1_) {
        super(p_i231953_1_);
    }

    @Override
    public boolean func_241855_a(ISeedReader p_230362_1_, ChunkGenerator p_230362_3_, Random p_230362_4_, BlockPos p_230362_5_, NoFeatureConfig p_230362_6_) {
        int i = 0;
        int j = p_230362_1_.getHeight(Heightmap.Type.OCEAN_FLOOR, p_230362_5_.getX(), p_230362_5_.getZ());
        BlockPos blockpos = new BlockPos(p_230362_5_.getX(), j, p_230362_5_.getZ());
        if (p_230362_1_.getBlockState(blockpos).isIn(Blocks.WATER)) {
            BlockState blockstate = SushiContent.Blocks.SEAWEED.get().getDefaultState();
            BlockState blockstate1 = SushiContent.Blocks.SEAWEED_PLANT.get().getDefaultState();
            int k = 1 + p_230362_4_.nextInt(10);

            for (int l = 0; l <= k; ++l) {
                if (p_230362_1_.getBlockState(blockpos).isIn(Blocks.WATER) && p_230362_1_.getBlockState(blockpos.up()).isIn(Blocks.WATER) && blockstate1.isValidPosition(p_230362_1_, blockpos)) {
                    if (l == k) {
                        p_230362_1_.setBlockState(blockpos, blockstate.with(KelpTopBlock.AGE, Integer.valueOf(p_230362_4_.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        p_230362_1_.setBlockState(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.down();
                    if (blockstate.isValidPosition(p_230362_1_, blockpos1) && !p_230362_1_.getBlockState(blockpos1.down()).isIn(Blocks.KELP)) {
                        p_230362_1_.setBlockState(blockpos1, blockstate.with(KelpTopBlock.AGE, Integer.valueOf(p_230362_4_.nextInt(4) + 20)), 2);
                        ++i;
                    }
                    break;
                }

                blockpos = blockpos.up();
            }
        }

        return i > 0;
    }
}
