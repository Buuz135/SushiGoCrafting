package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.item.FoodItem;
import net.minecraft.item.Item;

import java.util.*;

public class FoodHelper {

    public static HashMap<String, List<FoodItem>> REGISTERED = new LinkedHashMap<>();

    public static List<FoodItem> generateFood(IFoodType type) {
        List<FoodItem> items = new ArrayList<>();
        items.addAll(generate(type, type.getFoodIngredients()));
        items.forEach(item -> System.out.println(item.getIngredientList()));
        REGISTERED.put(type.getName(), items);
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
            for (IFoodIngredient iFoodIngredient : foodIngredients.get(0)) {
                List<FoodItem> all = generate(type, foodIngredients.subList(1, foodIngredients.size()));
                for (FoodItem item : all) {
                    if (item != null) item.getIngredientList().add(0, iFoodIngredient);
                }
                items.addAll(all);
            }
        }
        return items;
    }

    public static String getName(FoodItem item) {
        List<String> names = new ArrayList<>();
        for (int nameIndex : item.getType().getNameIndex()) {
            if (!item.getIngredientList().get(nameIndex).isEmpty())
                names.add(item.getIngredientList().get(nameIndex).getName());
        }
        return String.join("_", names) + "_" + item.getType().getName();
    }

    public static List<IFoodType> getAllFoodTypes() {
        return Arrays.asList(FoodType.values());
    }

    public static Optional<IFoodType> getTypeFromName(String name) {
        for (IFoodType foodType : FoodHelper.getAllFoodTypes()) {
            if (foodType.getName().equalsIgnoreCase(name)) {
                return Optional.of(foodType);
            }
        }
        return Optional.empty();
    }

    public static FoodItem getFoodFromIngredients(String type, List<IFoodIngredient> foodIngredients) {
        for (FoodItem foodItem : REGISTERED.get(type)) {
            if (foodIngredients.size() == foodItem.getIngredientList().size()) {
                boolean allMatch = true;
                for (int i = 0; i < foodIngredients.size(); i++) {
                    if (!foodIngredients.get(i).equals(foodItem.getIngredientList().get(i))) {
                        allMatch = false;
                    }
                }
                if (allMatch) {
                    return foodItem;
                }
            }
        }
        return null;
    }

}
