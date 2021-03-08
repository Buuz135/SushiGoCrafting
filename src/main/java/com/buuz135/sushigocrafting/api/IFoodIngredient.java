package com.buuz135.sushigocrafting.api;

import net.minecraft.item.Item;

public interface IFoodIngredient {

    Item getItem();

    String getName();

    boolean isEmpty();
}
