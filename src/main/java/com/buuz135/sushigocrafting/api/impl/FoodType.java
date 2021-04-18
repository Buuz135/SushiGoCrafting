package com.buuz135.sushigocrafting.api.impl;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FoodType implements IFoodType {

    public static FoodType MAKI = new FoodType("maki", new int[]{2},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(70 + integer * 10, 76 - integer * 28);
            }, of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.AVOCADO, FoodIngredient.CUCUMBER, FoodIngredient.CRAB));
    public static FoodType GUNKAN = new FoodType("gunkan", new int[]{2}, integer -> {
        if (integer > 2)
            return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
        return Pair.of(70 + integer * 10, 76 - integer * 28);
    },
            of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.WAKAME));
    public static FoodType CALIFORNIA = new FoodType("california", new int[]{0, 4, 5},
            integer -> {
                if (integer == 0)
                    return Pair.of(20, 40);
                if (integer > 3)
                    return Pair.of(35 + (integer - 3) * 25, 45 - (integer - 3) * 8);
                //return Pair.of(66 + integer * 10, 40 - (integer - 3) * 22);
                return Pair.of(30 + integer * 25, 77 - integer * 8);
            }, of(FoodIngredient.TOBIKO, FoodIngredient.EMPTY),
            of(FoodIngredient.RICE),
            of(FoodIngredient.NORI),
            of(FoodIngredient.AVOCADO),
            of(FoodIngredient.TUNA_FILLET, FoodIngredient.SALMON_FILLET, FoodIngredient.CRAB),
            of(FoodIngredient.CUCUMBER, FoodIngredient.EMPTY, FoodIngredient.CHEESE));
    public static FoodType NIGIRI = new FoodType("nigiri", new int[]{1},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(70 + integer * 10, 76 - integer * 28);
            },
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.SHRIMP),
            of(FoodIngredient.NORI));
    public static FoodType ONIGIRI = new FoodType("onigiri", new int[]{1},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(70 + integer * 10, 76 - integer * 28);
            },
            of(FoodIngredient.RICE),
            of(FoodIngredient.EMPTY),
            of(FoodIngredient.NORI));
    public static FoodType TEMAKI = new FoodType("temaki", new int[]{3},
            integer -> {
                if (integer > 3)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(70 + integer * 10, 78 - integer * 20);
            },
            of(FoodIngredient.NORI),
            of(FoodIngredient.AVOCADO),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.SHRIMP, FoodIngredient.CHICKEN)
    );

    public FoodType(String name, int[] index, Function<Integer, Pair<Integer, Integer>> slotPos, IFoodIngredient[]... ingredients) {
        this.index = index;
        this.name = name;
        this.slotPos = slotPos;
        this.ingredients = new ArrayList<>();
        if (ingredients != null) {
            for (IFoodIngredient[] ingredient : ingredients) {
                this.ingredients.add(ingredient);
            }
        }
        FoodAPI.get().addFoodType(this);
    }

    private final int[] index;
    private final String name;
    private List<IFoodIngredient[]> ingredients;
    private final Function<Integer, Pair<Integer, Integer>> slotPos;

    public static void init() {
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
