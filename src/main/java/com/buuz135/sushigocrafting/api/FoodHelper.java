package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.item.FoodItem;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class FoodHelper {

    public static List<FoodItem> generateFood(IFoodType type) {
        List<FoodItem> items = new ArrayList<>();
        items.addAll(generate(type, type.getFoodIngredients()));
        items.forEach(item -> System.out.println(item.getIngredientList()));
        return items;
    }

    private static List<FoodItem> generate(IFoodType type, List<IFoodIngredient[]> foodIngredients) {
        List<FoodItem> items = new ArrayList<>();
        if (foodIngredients.size() == 1) {
            for (IFoodIngredient iFoodIngredient : foodIngredients.get(0)) {
                FoodItem item = new FoodItem(new Item.Properties().group(SushiGoCrafting.TAB), type);
                item.getIngredientList().add(iFoodIngredient);
                items.add(item);
            }
        } else {
            items.addAll(generate(type, foodIngredients.subList(1, foodIngredients.size())));
            for (IFoodIngredient iFoodIngredient : foodIngredients.get(0)) {
                for (FoodItem item : items) {
                    item.getIngredientList().add(0, iFoodIngredient);
                }
            }
        }
        return items;
    }

    public static String getName(FoodItem item) {
        System.out.println(item.getIngredientList().get(item.getType().getNameIndex()).getName() + "_" + item.getType().getName());
        return item.getIngredientList().get(item.getType().getNameIndex()).getName() + "_" + item.getType().getName();
    }
}
