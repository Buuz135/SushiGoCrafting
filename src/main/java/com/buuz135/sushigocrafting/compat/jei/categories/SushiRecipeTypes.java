package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import mezz.jei.api.recipe.types.IRecipeType;

public class SushiRecipeTypes {

    public static IRecipeType<CuttingBoardRecipe> CUTTING_BOARD = IRecipeType.create(SushiGoCrafting.MOD_ID, "cutting_board", CuttingBoardRecipe.class);
    public static IRecipeType<FermentingBarrelRecipe> FERMENTING_BARREL = IRecipeType.create(SushiGoCrafting.MOD_ID, "fermenting_barrel", FermentingBarrelRecipe.class);
    public static IRecipeType<RiceCookerCategory.RiceCookerRecipe> RICE_COOKER = IRecipeType.create(SushiGoCrafting.MOD_ID, "rice_cooker", RiceCookerCategory.RiceCookerRecipe.class);
    public static IRecipeType<RollerCategory.Recipe> ROLLER = IRecipeType.create(SushiGoCrafting.MOD_ID, "roller", RollerCategory.Recipe.class);

}
