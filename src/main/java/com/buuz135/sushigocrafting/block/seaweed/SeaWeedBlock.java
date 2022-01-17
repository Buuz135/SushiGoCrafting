package com.buuz135.sushigocrafting.block.seaweed;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.KelpPlantBlock;

public class SeaWeedBlock extends KelpPlantBlock {

    public SeaWeedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) SushiContent.Blocks.SEAWEED.get();
    }
}
