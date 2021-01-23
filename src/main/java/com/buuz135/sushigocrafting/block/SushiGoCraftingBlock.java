package com.buuz135.sushigocrafting.block;

import com.hrznstudio.titanium.block.BasicBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class SushiGoCraftingBlock extends BasicBlock {

    public SushiGoCraftingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Item asItem() {
        if (super.asItem() == null) setItem((BlockItem) Item.getItemFromBlock(this));
        return super.asItem();
    }

}