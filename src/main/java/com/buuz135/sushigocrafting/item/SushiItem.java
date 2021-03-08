package com.buuz135.sushigocrafting.item;

import net.minecraft.item.Item;

public class SushiItem extends Item {

    private final String category;

    public SushiItem(Properties properties, String category) {
        super(properties);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
