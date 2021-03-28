package com.buuz135.sushigocrafting.api.impl;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FoodAPI {

    private static FoodAPI INSTACE = new FoodAPI();
    private List<IFoodType> foodTypes = new ArrayList<>();
    private List<IFoodIngredient> foodIngredients = new ArrayList<>();

    private FoodAPI() {

    }

    public FoodAPI init() {
        FoodIngredient.init();
        FoodType.init();
        return this;
    }

    public static FoodAPI get() {
        return INSTACE;
    }

    public void addFoodIngredient(IFoodIngredient iFoodIngredient) {
        foodIngredients.add(iFoodIngredient);
    }

    public void addFoodType(IFoodType iFoodType) {
        foodTypes.add(iFoodType);
    }

    @Nonnull
    public IFoodIngredient getIngredientFromItem(Item item) {
        for (IFoodIngredient value : foodIngredients) {
            if (value.isEmpty()) continue;
            if (value.getItem().equals(item)) return value;
        }
        return FoodIngredient.EMPTY;
    }

    @Nonnull
    public IFoodIngredient getIngredientFromName(String name) {
        for (IFoodIngredient value : foodIngredients) {
            if (value.getName().equals(name)) return value;
        }
        return FoodIngredient.EMPTY;
    }

    public List<IFoodType> getFoodTypes() {
        return Collections.unmodifiableList(foodTypes);
    }

    public List<IFoodIngredient> getFoodIngredient() {
        return Collections.unmodifiableList(foodIngredients);
    }

    public Optional<IFoodType> getTypeFromName(String name) {
        for (IFoodType foodType : foodTypes) {
            if (foodType.getName().equalsIgnoreCase(name)) {
                return Optional.of(foodType);
            }
        }
        return Optional.empty();
    }
}
