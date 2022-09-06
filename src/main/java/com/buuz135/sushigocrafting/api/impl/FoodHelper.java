package com.buuz135.sushigocrafting.api.impl;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.item.FoodItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FoodHelper {

    public static HashMap<String, List<RegistryObject<Item>>> REGISTERED = new LinkedHashMap<>();

    public static List<FoodData> generateFood(IFoodType type) {
        List<FoodData> items = new ArrayList<>();
        items.addAll(generate(type, type.getFoodIngredients()));
        return items;
    }

    private static List<FoodData> generate(IFoodType type, List<IFoodIngredient[]> foodIngredients) {
        List<FoodData> items = new ArrayList<>();
        if (foodIngredients.size() == 1) {
            for (IFoodIngredient iFoodIngredient : foodIngredients.get(0)) {
                FoodData item = new FoodData(type);
                item.getFoodIngredients().add(iFoodIngredient);
                items.add(item);
            }
        } else {
            for (IFoodIngredient iFoodIngredient : foodIngredients.get(0)) {
                List<FoodData> all = generate(type, foodIngredients.subList(1, foodIngredients.size()));
                for (FoodData item : all) {
                    if (item != null) item.getFoodIngredients().add(0, iFoodIngredient);
                }
                items.addAll(all);
            }
        }
        return items;
    }

    public static String getName(FoodData item) {
        List<String> names = new ArrayList<>();
        for (int nameIndex : item.getFoodType().getNameIndex()) {
            if (!item.getFoodIngredients().get(nameIndex).isEmpty())
                names.add(item.getFoodIngredients().get(nameIndex).getName());
        }
        names.add(item.getFoodType().getName());
        return String.join("_", names);
    }

    public static FoodItem getFoodFromIngredients(String type, List<IFoodIngredient> foodIngredients) {
        for (RegistryObject<Item> foodItem : REGISTERED.get(type)) {
            if (foodIngredients.size() == ((FoodItem)foodItem.get()).getIngredientList().size()) {
                boolean allMatch = true;
                for (int i = 0; i < foodIngredients.size(); i++) {
                    if (!foodIngredients.get(i).equals(((FoodItem)foodItem.get()).getIngredientList().get(i))) {
                        allMatch = false;
                    }
                }
                if (allMatch) {
                    return (FoodItem) foodItem.get();
                }
            }
        }
        return null;
    }

    public static class FoodData{

        private List<IFoodIngredient> foodIngredients;
        private final IFoodType foodType;

        public FoodData(IFoodType foodType) {
            this.foodType = foodType;
            this.foodIngredients = new ArrayList<>();
        }

        public List<IFoodIngredient> getFoodIngredients() {
            return foodIngredients;
        }

        public IFoodType getFoodType() {
            return foodType;
        }
    }

}
