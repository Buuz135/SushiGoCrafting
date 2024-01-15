package com.buuz135.sushigocrafting.client;

import com.buuz135.sushigocrafting.api.IFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.impl.renderer.CubeFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.impl.renderer.FlatSheetFoodIngredientRenderer;
import com.buuz135.sushigocrafting.api.impl.renderer.StackFoodIngredientRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;

import java.awt.*;

public class FoodRenderers {

    public static IFoodIngredientRenderer RICE = new FlatSheetFoodIngredientRenderer(0.01f, new Color(230, 237, 232));
    public static IFoodIngredientRenderer NORI = new FlatSheetFoodIngredientRenderer(0.005f, new Color(19, 28, 13));
    public static IFoodIngredientRenderer TUNA_FILLET = new CubeFoodIngredientRenderer(0.01f, new Color(168, 39, 47));
    public static IFoodIngredientRenderer SALMON_FILLET = new CubeFoodIngredientRenderer(0.01f, new Color(255, 117, 59));
    public static IFoodIngredientRenderer AVOCADO = new CubeFoodIngredientRenderer(0.01f, new Color(160, 179, 79));
    public static IFoodIngredientRenderer CUCUMBER = new CubeFoodIngredientRenderer(0.01f, new Color(227, 241, 199));
    public static IFoodIngredientRenderer CRAB = new CubeFoodIngredientRenderer(0.01f, new Color(214, 34, 43));
    public static IFoodIngredientRenderer WAKAME = new CubeFoodIngredientRenderer(0.01f, new Color(82, 127, 29));
    public static IFoodIngredientRenderer TOBIKO = new CubeFoodIngredientRenderer(0.01f, new Color(179, 18, 26));
    public static IFoodIngredientRenderer CHEESE = new CubeFoodIngredientRenderer(0.01f, new Color(237, 208, 125));
    public static IFoodIngredientRenderer SHRIMP = new CubeFoodIngredientRenderer(0.01f, new Color(200, 91, 89));
    public static IFoodIngredientRenderer CHICKEN = new CubeFoodIngredientRenderer(0.01f, new Color(255, 149, 1));
    public static IFoodIngredientRenderer SOY_SAUCE = new StackFoodIngredientRenderer(SushiContent.Items.SOY_SAUCE, 0.92f, 0f, 0.15f, -20f, 0f);
    public static IFoodIngredientRenderer WASABI = new StackFoodIngredientRenderer(SushiContent.Items.WASABI_PASTE, 0.035f, -0.02f, 0.2f, 20, -90);
}
