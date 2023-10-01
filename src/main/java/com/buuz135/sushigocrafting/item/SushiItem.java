package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import net.minecraft.world.item.Item;

public class SushiItem extends Item {

    private final String category;

    public SushiItem(Properties properties, String category) {
        super(properties);
        this.category = category;
        SushiGoCrafting.TAB.getTabList().add(this);
    }

    public String getCategory() {
        return category;
    }
}
