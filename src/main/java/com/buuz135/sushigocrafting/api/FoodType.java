package com.buuz135.sushigocrafting.api;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public enum FoodType implements IFoodType {

    MAKI("maki", new int[]{2},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(70 + integer * 10, 76 - integer * 28);
            }, of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.AVOCADO, FoodIngredient.CUCUMBER, FoodIngredient.CRAB)),
    GUNKAN("gunkan", new int[]{2}, integer -> {
        return Pair.of(0, 0);
    },
            of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.WAKAME)),
    CALIFORNIA("california", new int[]{0, 4, 5},
            integer -> {
                if (integer > 2)
                    return Pair.of(66 + integer * 10, 40 - (integer - 3) * 22);
                return Pair.of(55 + integer * 25, 60 + integer * 8);
            }, of(FoodIngredient.TOBIKO, FoodIngredient.EMPTY),
            of(FoodIngredient.RICE),
            of(FoodIngredient.NORI),
            of(FoodIngredient.AVOCADO),
            of(FoodIngredient.TUNA_FILLET, FoodIngredient.SALMON_FILLET, FoodIngredient.CRAB),
            of(FoodIngredient.CUCUMBER, FoodIngredient.EMPTY, FoodIngredient.CHEESE));

    private final int[] index;
    private final String name;
    private List<IFoodIngredient[]> ingredients;
    private final Function<Integer, Pair<Integer, Integer>> slotPos;

    FoodType(String name, int[] index, Function<Integer, Pair<Integer, Integer>> slotPos, IFoodIngredient[]... ingredients) {
        this.index = index;
        this.name = name;
        this.slotPos = slotPos;
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
    public int[] getNameIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Function<Integer, Pair<Integer, Integer>> getSlotPosition() {
        return slotPos;
    }
}
