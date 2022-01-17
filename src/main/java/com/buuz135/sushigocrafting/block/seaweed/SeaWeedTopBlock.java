package com.buuz135.sushigocrafting.block.seaweed;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.KelpBlock;

public class SeaWeedTopBlock extends KelpBlock {

    public SeaWeedTopBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected Block getBodyBlock() {
        return SushiContent.Blocks.SEAWEED_PLANT.get();
    } //getBodyBlock

}
