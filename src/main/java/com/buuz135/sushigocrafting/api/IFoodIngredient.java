package com.buuz135.sushigocrafting.api;

import net.minecraft.item.Item;

public interface IFoodIngredient {

    Item getItem();

    String getName();

    public default boolean isEmpty() {
        return this == FoodIngredient.EMPTY;
    }

    IIngredientConsumer getIngredientConsumer();

    int getDefaultAmount();
}
