package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.api.impl.DietType;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface IFoodIngredient {

    Item getItem();

    String getName();

    default boolean isEmpty() {
        return this == FoodIngredient.EMPTY;
    }

    IIngredientConsumer getIngredientConsumer();

    int getDefaultAmount();

    IIngredientEffect getEffect();

    int getHungerValue();

    int getSaturationValue();

    DietType getDietType();

    Supplier<IFoodIngredientRenderer> getRenderer();

}
