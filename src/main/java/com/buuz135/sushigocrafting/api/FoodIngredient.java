package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum FoodIngredient implements IFoodIngredient {
    EMPTY("empty", null, false, null),
    RICE("rice", SushiContent.Items.COOKED_RICE, false, null),
    NORI("nori", SushiContent.Items.NORI_SHEET, false, null),
    TUNA_FILLET("tuna", SushiContent.Items.RAW_TUNA_FILLET, true, () -> Ingredient.fromItems(SushiContent.Items.RAW_TUNA.get())),
    SALMON_FILLET("salmon", SushiContent.Items.RAW_SALMON_FILLET, true, () -> Ingredient.fromItems(Items.SALMON)),
    AVOCADO("avocado", SushiContent.Items.AVOCADO_SLICES, true, () -> Ingredient.fromItems(SushiContent.Items.AVOCADO.get())),
    CUCUMBER("cucumber", SushiContent.Items.CUCUMBER_SLICES, true, () -> Ingredient.fromItems(SushiContent.Items.CUCUMBER.get())),
    SESAME("sesame", SushiContent.Items.SESAME_SEED, false, null),
    CRAB("crab", SushiContent.Items.IMITATION_CRAB, true, () -> Ingredient.fromItems(Items.COD)),
    WAKAME("wakame", SushiContent.Items.IMITATION_CRAB, true, () -> Ingredient.fromItems(Items.KELP)),
    TOBIKO("tobiko", SushiContent.Items.TOBIKO, false, null),
    CHEESE("cheese", SushiContent.Items.CHEESE, false, null),
    SHRIMP("shrimp", SushiContent.Items.SHRIMP, true, () -> Ingredient.fromItems(SushiContent.Items.SHRIMP.get())),
    CHICKEN("chicken", () -> Items.COOKED_CHICKEN, false, null);

    private final Supplier<? extends Item> item;
    private final String name;
    private final boolean needsChoppingRecipe;
    private final Supplier<Ingredient> tag;

    FoodIngredient(String name, Supplier<? extends Item> item, boolean needsChoppingRecipe, Supplier<Ingredient> tag) {
        this.name = name;
        this.item = item;
        this.needsChoppingRecipe = needsChoppingRecipe;
        this.tag = tag;
    }

    @Override
    public Item getItem() {
        return item.get();
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Nullable
    public static FoodIngredient fromName(String name) {
        for (FoodIngredient value : values()) {
            if (value.getName().equals(name)) return value;
        }
        return null;
    }

    public boolean isNeedsChoppingRecipe() {
        return needsChoppingRecipe;
    }

    @Nullable
    public static FoodIngredient fromItem(Item item) {
        for (FoodIngredient value : values()) {
            if (value.getItem().equals(item)) return value;
        }
        return null;
    }

    public Supplier<Ingredient> getTag() {
        return tag;
    }
}
