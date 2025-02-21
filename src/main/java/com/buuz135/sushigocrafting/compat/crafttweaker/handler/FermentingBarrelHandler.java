package com.buuz135.sushigocrafting.compat.crafttweaker.handler;


import com.blamejared.crafttweaker.api.fluid.MCFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

@IRecipeHandler.For(FermentingBarrelRecipe.class)
public class FermentingBarrelHandler implements IRecipeHandler<FermentingBarrelRecipe> {


    @Override
    public String dumpToCommandString(IRecipeManager<? super FermentingBarrelRecipe> iRecipeManager, RegistryAccess registryAccess, RecipeHolder<FermentingBarrelRecipe> recipe) {
        return String.format("<recipetype:sushigocrafting:fermenting_barrel>.addRecipe(%s, %s, %s, %s);", StringUtil.quoteAndEscape(recipe.id()), IIngredient.fromIngredient(recipe.value().getInput()).getCommandString(), new MCFluidStack(recipe.value().getFluid()).getCommandString(), new MCItemStack(recipe.value().getOutput()).getCommandString());

    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super FermentingBarrelRecipe> iRecipeManager, FermentingBarrelRecipe fermentingBarrelRecipe, U u) {
        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super FermentingBarrelRecipe> iRecipeManager, RegistryAccess registryAccess, FermentingBarrelRecipe fermentingBarrelRecipe) {
        return Optional.empty();
    }

    @Override
    public Optional<FermentingBarrelRecipe> recompose(IRecipeManager<? super FermentingBarrelRecipe> iRecipeManager, RegistryAccess registryAccess, IDecomposedRecipe iDecomposedRecipe) {
        return Optional.empty();
    }


}
