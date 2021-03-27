package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

public class FoodIngredient implements IFoodIngredient {

    public static FoodIngredient EMPTY = new FoodIngredient("empty", null, 0, IIngredientConsumer.WEIGHT, null);
    public static FoodIngredient RICE = new FoodIngredient("rice", SushiContent.Items.COOKED_RICE, 10, IIngredientConsumer.WEIGHT, null);
    public static FoodIngredient NORI = new FoodIngredient("nori", SushiContent.Items.NORI_SHEET, 1, IIngredientConsumer.STACK, null);
    public static FoodIngredient TUNA_FILLET = new FoodIngredient("tuna", SushiContent.Items.RAW_TUNA_FILLET, 10, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(SushiContent.Items.RAW_TUNA.get()));
    public static FoodIngredient SALMON_FILLET = new FoodIngredient("salmon", SushiContent.Items.RAW_SALMON_FILLET, 10, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(Items.SALMON));
    public static FoodIngredient AVOCADO = new FoodIngredient("avocado", SushiContent.Items.AVOCADO_SLICES, 15, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(SushiContent.Items.AVOCADO.get()));
    public static FoodIngredient CUCUMBER = new FoodIngredient("cucumber", SushiContent.Items.CUCUMBER_SLICES, 5, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(SushiContent.Items.CUCUMBER.get()));
    public static FoodIngredient SESAME = new FoodIngredient("sesame", SushiContent.Items.SESAME_SEED, 2, IIngredientConsumer.WEIGHT, null);
    public static FoodIngredient CRAB = new FoodIngredient("crab", SushiContent.Items.IMITATION_CRAB, 10, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(Items.COD));
    public static FoodIngredient WAKAME = new FoodIngredient("wakame", SushiContent.Items.IMITATION_CRAB, 10, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(Items.KELP));
    public static FoodIngredient TOBIKO = new FoodIngredient("tobiko", SushiContent.Items.TOBIKO, 4, IIngredientConsumer.WEIGHT, null);
    public static FoodIngredient CHEESE = new FoodIngredient("cheese", SushiContent.Items.CHEESE, 4, IIngredientConsumer.WEIGHT, null);
    public static FoodIngredient SHRIMP = new FoodIngredient("shrimp", SushiContent.Items.SHRIMP, 10, IIngredientConsumer.WEIGHT, () -> Ingredient.fromItems(SushiContent.Items.SHRIMP.get()));
    public static FoodIngredient CHICKEN = new FoodIngredient("chicken", () -> Items.COOKED_CHICKEN, 10, IIngredientConsumer.WEIGHT, null);

    public FoodIngredient(String name, Supplier<? extends Item> item, int defaultAmount, IIngredientConsumer ingredientConsumer, Supplier<Ingredient> ingredientSupplier) {
        this.name = name;
        this.item = item;
        this.defaultAmount = defaultAmount;
        this.ingredientConsumer = ingredientConsumer;
        this.ingredientSupplier = ingredientSupplier;
        FoodAPI.get().addFoodIngredient(this);
    }

    private final Supplier<? extends Item> item;
    private final String name;
    private final int defaultAmount;
    private final IIngredientConsumer ingredientConsumer;
    private final Supplier<Ingredient> ingredientSupplier;

    public static void init() {
    }

    @Override
    public Item getItem() {
        return item.get();
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean needsChoppingRecipe() {
        return ingredientSupplier != null;
    }

    public Supplier<Ingredient> getInput() {
        return ingredientSupplier;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public IIngredientConsumer getIngredientConsumer() {
        return ingredientConsumer;
    }
}
