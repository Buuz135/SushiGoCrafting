package com.buuz135.sushigocrafting.block.seaweed;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.AbstractTopPlantBlock;
import net.minecraft.block.KelpBlock;

public class SeaWeedBlock extends KelpBlock {

    public SeaWeedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractTopPlantBlock getTopPlantBlock() {
        return (AbstractTopPlantBlock) SushiContent.Blocks.SEAWEED.get();
    }
}
