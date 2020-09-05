package com.buuz135.sushigocrafting.api;

import java.util.ArrayList;
import java.util.List;

public enum FoodType implements IFoodType {

    MAKI("maki", 2,
            of(FoodIngredient.DRY_SEAWEED),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.AVOCADO, FoodIngredient.CUCUMBER, FoodIngredient.CRAB));

    private final int index;
    private final String name;
    private List<IFoodIngredient[]> ingredients;

    FoodType(String name, int index, IFoodIngredient[]... ingredients) {
        this.index = index;
        this.name = name;
        this.ingredients = new ArrayList<>();
        if (ingredients != null) {
            for (IFoodIngredient[] ingredient : ingredients) {
                this.ingredients.add(ingredient);
            }
        }
    }

    public static IFoodIngredient[] of(IFoodIngredient... ingredients) {
        return ingredients;
    }

    @Override
    public List<IFoodIngredient[]> getFoodIngredients() {
        return ingredients;
    }

    @Override
    public int getNameIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }
}
