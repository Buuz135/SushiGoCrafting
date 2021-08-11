package com.buuz135.sushigocrafting.api.impl;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FoodType implements IFoodType {

    public static FoodType MAKI = new FoodType("maki", new int[]{2},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(78 - (int) (30 * (integer - (2 / 2D))), 49);
            }, integer -> {
        switch (integer) {
            case 0:
                return new ItemStack(FoodIngredient.NORI.getItem());
            case 1:
                return new ItemStack(FoodIngredient.RICE.getItem());
        }
        return ItemStack.EMPTY;
    }, of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.AVOCADO, FoodIngredient.CUCUMBER, FoodIngredient.CRAB));
    public static FoodType GUNKAN = new FoodType("gunkan", new int[]{2}, integer -> {
        if (integer > 2)
            return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
        return Pair.of(78 - (int) (30 * (integer - (2 / 2D))), 49);
    },
            integer -> {
                switch (integer) {
                    case 0:
                        return new ItemStack(FoodIngredient.NORI.getItem());
                    case 1:
                        return new ItemStack(FoodIngredient.RICE.getItem());
                }
                return ItemStack.EMPTY;
            }, of(FoodIngredient.NORI),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.WAKAME));
    public static FoodType CALIFORNIA = new FoodType("california", new int[]{0, 4, 5},
            integer -> {
                if (integer == 0)
                    return Pair.of(12, 77);
                return Pair.of(78 - (int) (30 * ((integer - 1) - (4 / 2D))), 49);
            }, integer -> {
        switch (integer) {
            case 0:
                return new ItemStack(FoodIngredient.TOBIKO.getItem());
            case 1:
                return new ItemStack(FoodIngredient.RICE.getItem());
            case 2:
                return new ItemStack(FoodIngredient.NORI.getItem());
            case 3:
                return new ItemStack(FoodIngredient.AVOCADO.getItem());
        }
        return ItemStack.EMPTY;
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
                return Pair.of(78 - (int) (30 * (integer - (2 / 2D))), 49);
            },
            integer -> {
                switch (integer) {
                    case 0:
                        return new ItemStack(FoodIngredient.RICE.getItem());
                    case 2:
                        return new ItemStack(FoodIngredient.NORI.getItem());
                }
                return ItemStack.EMPTY;
            }, of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.SHRIMP),
            of(FoodIngredient.NORI));
    public static FoodType ONIGIRI = new FoodType("onigiri", new int[]{1},
            integer -> {
                if (integer > 2)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(78 - (int) (30 * (integer - (2 / 2D))), 49);
            },
            integer -> {
                switch (integer) {
                    case 0:
                        return new ItemStack(FoodIngredient.RICE.getItem());
                    case 2:
                        return new ItemStack(FoodIngredient.NORI.getItem());
                }
                return ItemStack.EMPTY;
            }, of(FoodIngredient.RICE),
            of(FoodIngredient.EMPTY),
            of(FoodIngredient.NORI));
    public static FoodType TEMAKI = new FoodType("temaki", new int[]{3},
            integer -> {
                if (integer > 3)
                    return Pair.of(-Integer.MAX_VALUE, -Integer.MAX_VALUE);
                return Pair.of(78 - (int) (30 * (integer - (3 / 2D))), 49);
            },
            integer -> {
                switch (integer) {
                    case 0:
                        return new ItemStack(FoodIngredient.NORI.getItem());
                    case 1:
                        return new ItemStack(FoodIngredient.AVOCADO.getItem());
                    case 2:
                        return new ItemStack(FoodIngredient.RICE.getItem());
                }
                return ItemStack.EMPTY;
            }, of(FoodIngredient.NORI),
            of(FoodIngredient.AVOCADO),
            of(FoodIngredient.RICE),
            of(FoodIngredient.SALMON_FILLET, FoodIngredient.TUNA_FILLET, FoodIngredient.SHRIMP, FoodIngredient.CHICKEN)
    );
    private final Function<Integer, ItemStack> slotStackRender;

    private final int[] index;
    private final String name;
    private final Function<Integer, Pair<Integer, Integer>> slotPos;
    private List<IFoodIngredient[]> ingredients;

    public FoodType(String name, int[] index, Function<Integer, Pair<Integer, Integer>> slotPos, Function<Integer, ItemStack> slotStackRender, IFoodIngredient[]... ingredients) {
        this.index = index;
        this.name = name;
        this.slotPos = slotPos;
        this.slotStackRender = slotStackRender;
        this.ingredients = new ArrayList<>();
        if (ingredients != null) {
            for (IFoodIngredient[] ingredient : ingredients) {
                this.ingredients.add(ingredient);
            }
        }
        FoodAPI.get().addFoodType(this);
    }

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

    public Function<Integer, ItemStack> getSlotStackRender() {
        return slotStackRender;
    }
}
