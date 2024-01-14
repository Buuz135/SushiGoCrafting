package com.buuz135.sushigocrafting.api.impl;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.IIngredientConsumer;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.AddIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.renderer.CubeFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.impl.renderer.FlatSheetFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.impl.renderer.StackFoodIngredientRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.awt.*;
import java.util.function.Supplier;

public class FoodIngredient implements IFoodIngredient {

    public static FoodIngredient EMPTY = new FoodIngredient("empty", null, 0, IIngredientConsumer.WEIGHT, null, null, 0, 0, DietType.SUGARS, null);
    public static FoodIngredient RICE = new FoodIngredient("rice", SushiContent.Items.COOKED_RICE, 30, IIngredientConsumer.WEIGHT, null,
            new ModifyIngredientEffect(1.75f, 0), 0, 2, DietType.GRAINS, new FlatSheetFoodIngredientRenderer(0.01f, new Color(230, 237, 232)));
    public static FoodIngredient NORI = new FoodIngredient("nori", SushiContent.Items.NORI_SHEET, 1, IIngredientConsumer.STACK, null,
            null, 1, 1, DietType.VEGETABLES, new FlatSheetFoodIngredientRenderer(0.005f, new Color(19, 28, 13)));
    public static FoodIngredient TUNA_FILLET = new FoodIngredient("tuna", SushiContent.Items.RAW_TUNA_FILLET, 30, IIngredientConsumer.WEIGHT, () -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "raw_fishes/tuna"))),
            new AddIngredientEffect(SushiContent.Effects.ACQUIRED_TASTE, 30, 0), 3, 1, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(168, 39, 47)));
    public static FoodIngredient SALMON_FILLET = new FoodIngredient("salmon", SushiContent.Items.RAW_SALMON_FILLET, 30, IIngredientConsumer.WEIGHT, () -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "raw_fishes/salmon"))),
            new AddIngredientEffect(SushiContent.Effects.SMALL_BITES, 30, 0), 3, 1, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(255, 117, 59)));
    public static FoodIngredient AVOCADO = new FoodIngredient("avocado", SushiContent.Items.AVOCADO_SLICES, 45, IIngredientConsumer.WEIGHT, () -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "fruits/avocado"))),
            new ModifyIngredientEffect(1.20f, 1), 2, 2, DietType.FRUITS, new CubeFoodIngredientRenderer(0.01f, new Color(160, 179, 79)));
    public static FoodIngredient CUCUMBER = new FoodIngredient("cucumber", SushiContent.Items.CUCUMBER_SLICES, 15, IIngredientConsumer.WEIGHT, () -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "crops/cucumber"))),
            new ModifyIngredientEffect(1.25f, 0), 2, 2, DietType.VEGETABLES, new CubeFoodIngredientRenderer(0.01f, new Color(227, 241, 199)));
    public static FoodIngredient SESAME = new FoodIngredient("sesame", SushiContent.Items.SESAME_SEED, 6, IIngredientConsumer.WEIGHT, null,
            new ModifyIngredientEffect(2, 0), 1, 1, DietType.GRAINS, null);
    public static FoodIngredient CRAB = new FoodIngredient("crab", SushiContent.Items.IMITATION_CRAB, 30, IIngredientConsumer.WEIGHT, () -> Ingredient.of(Items.COD),
            new AddIngredientEffect(() -> MobEffects.MOVEMENT_SPEED, 3, 0), 2, 1, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(214, 34, 43)));
    public static FoodIngredient WAKAME = new FoodIngredient("wakame", () -> Items.SEAGRASS, 1, IIngredientConsumer.STACK, null,
            null, 0, 1, DietType.VEGETABLES, new CubeFoodIngredientRenderer(0.01f, new Color(82, 127, 29)));
    public static FoodIngredient TOBIKO = new FoodIngredient("tobiko", SushiContent.Items.TOBIKO, 12, IIngredientConsumer.WEIGHT, null,
            new ModifyIngredientEffect(1.50f, 0), 1, 2, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(179, 18, 26)));
    public static FoodIngredient CHEESE = new FoodIngredient("cheese", SushiContent.Items.CHEESE, 12, IIngredientConsumer.WEIGHT, null,
            new AddIngredientEffect(() -> MobEffects.SATURATION, 15, 0), 1, 2, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(237, 208, 125)));
    public static FoodIngredient SHRIMP = new FoodIngredient("shrimp", SushiContent.Items.SHRIMP, 30, IIngredientConsumer.WEIGHT, null,
            new AddIngredientEffect(SushiContent.Effects.STEADY_HANDS, 60, 0), 2, 1, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(200, 91, 89)));
    public static FoodIngredient CHICKEN = new FoodIngredient("chicken", () -> Items.COOKED_CHICKEN, 30, IIngredientConsumer.WEIGHT, null,
            new AddIngredientEffect(() -> MobEffects.SLOW_FALLING, 10, 0), 2, 1, DietType.PROTEINS, new CubeFoodIngredientRenderer(0.01f, new Color(255, 149, 1)));
    public static FoodIngredient SOY_SAUCE = new FoodIngredient("soy_sauce", SushiContent.Items.SOY_SAUCE, 4, IIngredientConsumer.WEIGHT, null,
            new ModifyIngredientEffect(1, 1), 0, 0, DietType.GRAINS, new StackFoodIngredientRenderer(SushiContent.Items.SOY_SAUCE, 0.92f, 0f, 0.15f, -20f, 0f));
    public static FoodIngredient WASABI = new FoodIngredient("wasabi", SushiContent.Items.WASABI_PASTE, 4, IIngredientConsumer.WEIGHT, () -> Ingredient.of(ItemTags.create(new ResourceLocation("forge", "crops/wasabi_root"))),
            new AddIngredientEffect(() -> MobEffects.FIRE_RESISTANCE, 10, 0), 0, 0, DietType.VEGETABLES, new StackFoodIngredientRenderer(SushiContent.Items.WASABI_PASTE, 0.035f, -0.02f, 0.2f, 20, -90));

    private final Supplier<? extends Item> item;
    private final String name;
    private final int defaultAmount;
    private final IIngredientConsumer ingredientConsumer;
    private final Supplier<Ingredient> ingredientSupplier;
    private final IIngredientEffect effect;
    private final int hungerValue;
    private final int saturationValue;
    private final DietType dietType;

    private final IFoodIngredientRenderer renderer;

    public FoodIngredient(String name, Supplier<? extends Item> item, int defaultAmount, IIngredientConsumer ingredientConsumer, Supplier<Ingredient> ingredientSupplier, IIngredientEffect effect, int foodValue, int saturationValue, DietType dietType, IFoodIngredientRenderer renderer) {
        this.name = name;
        this.item = item;
        this.defaultAmount = defaultAmount;
        this.ingredientConsumer = ingredientConsumer;
        this.ingredientSupplier = ingredientSupplier;
        this.effect = effect;
        this.hungerValue = foodValue;
        this.saturationValue = saturationValue;
        this.dietType = dietType;
        this.renderer = renderer;
        FoodAPI.get().addFoodIngredient(this);
    }

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

    @Override
    public IIngredientEffect getEffect() {
        return effect;
    }

    @Override
    public int getHungerValue() {
        return hungerValue;
    }

    @Override
    public int getSaturationValue() {
        return saturationValue;
    }

    @Override
    public DietType getDietType() {
        return dietType;
    }

    public IIngredientConsumer getIngredientConsumer() {
        return ingredientConsumer;
    }

    @Override
    public IFoodIngredientRenderer getRenderer() {
        return renderer;
    }
}
