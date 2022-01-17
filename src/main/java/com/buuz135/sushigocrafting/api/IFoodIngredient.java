package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.api.impl.DietType;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import net.minecraft.world.item.Item;

public interface IFoodIngredient {

    Item getItem();

    String getName();

    public default boolean isEmpty() {
        return this == FoodIngredient.EMPTY;
    }

    IIngredientConsumer getIngredientConsumer();

    int getDefaultAmount();

    IIngredientEffect getEffect();

    int getHungerValue();

    int getSaturationValue();

    DietType getDietType();
}
