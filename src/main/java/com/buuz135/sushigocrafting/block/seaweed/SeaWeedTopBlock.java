package com.buuz135.sushigocrafting.block.seaweed;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.Block;
import net.minecraft.block.KelpTopBlock;

public class SeaWeedTopBlock extends KelpTopBlock {

    public SeaWeedTopBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected Block func_230330_d_() {
        return SushiContent.Blocks.SEAWEED_PLANT.get();
    }

}
