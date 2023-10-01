package com.buuz135.sushigocrafting.compat.crafttweaker.handler;


import com.blamejared.crafttweaker.api.fluid.MCFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

@IRecipeHandler.For(FermentingBarrelRecipe.class)
public class FermentingBarrelHandler implements IRecipeHandler<FermentingBarrelRecipe> {

    @Override
    public String dumpToCommandString(IRecipeManager manager, FermentingBarrelRecipe recipe) {
        return String.format("<recipetype:sushigocrafting:fermenting_barrel>.addRecipe(%s, %s, %s, %s);", StringUtil.quoteAndEscape(recipe.getId()), IIngredient.fromIngredient(recipe.getInput()).getCommandString(), new MCFluidStack(recipe.getFluid()).getCommandString(), new MCItemStack(recipe.getOutput()).getCommandString());
    }

    /*@Override
    public Optional<Function<ResourceLocation, FermentingBarrelRecipe>> replaceIngredients(IRecipeManager manager, FermentingBarrelRecipe recipe, List<IReplacementRule> rules) {
        Optional<Ingredient> input = IRecipeHandler.attemptReplacing(recipe.getInput(), Ingredient.class, recipe, rules);
        if (input.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(id -> new FermentingBarrelRecipe(id, input.orElseGet(recipe::getInput), recipe.getFluid(), recipe.getOutput()));
    }*/


    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super FermentingBarrelRecipe> manager, FermentingBarrelRecipe firstRecipe, U secondRecipe) {
        return IngredientUtil.canConflict(firstRecipe.getInput(), ((FermentingBarrelRecipe) secondRecipe).getInput());
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super FermentingBarrelRecipe> manager, FermentingBarrelRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<FermentingBarrelRecipe> recompose(IRecipeManager<? super FermentingBarrelRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}
