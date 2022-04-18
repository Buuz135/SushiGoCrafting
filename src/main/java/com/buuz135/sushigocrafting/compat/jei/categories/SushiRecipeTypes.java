package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import mezz.jei.api.recipe.RecipeType;

public class SushiRecipeTypes {

    public static RecipeType<CuttingBoardRecipe> CUTTING_BOARD = RecipeType.create(SushiGoCrafting.MOD_ID, "cutting_board", CuttingBoardRecipe.class);
    public static RecipeType<FermentingBarrelRecipe> FERMENTING_BARREL = RecipeType.create(SushiGoCrafting.MOD_ID, "fermenting_barrel", FermentingBarrelRecipe.class);
    public static RecipeType<RiceCookerCategory.RiceCookerRecipe> RICE_COOKER = RecipeType.create(SushiGoCrafting.MOD_ID, "rice_cooker", RiceCookerCategory.RiceCookerRecipe.class);
    public static RecipeType<RollerCategory.Recipe> ROLLER = RecipeType.create(SushiGoCrafting.MOD_ID, "roller", RollerCategory.Recipe.class);

}
