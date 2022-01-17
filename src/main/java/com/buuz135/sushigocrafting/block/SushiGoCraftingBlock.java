package com.buuz135.sushigocrafting.block;

import com.hrznstudio.titanium.block.BasicBlock;
import net.minecraft.world.item.Item;

public class SushiGoCraftingBlock extends BasicBlock {

    public SushiGoCraftingBlock(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public Item asItem() {
        return Item.byBlock(this);
    }

}