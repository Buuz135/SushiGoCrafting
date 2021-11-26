package com.buuz135.sushigocrafting.compat.crafttweaker.handler;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.fluid.MCFluidStack;
import com.blamejared.crafttweaker.impl.helper.IngredientHelper;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@IRecipeHandler.For(FermentingBarrelRecipe.class)
public class FermentingBarrelHandler implements IRecipeHandler<FermentingBarrelRecipe> {
    
    @Override
    public String dumpToCommandString(IRecipeManager manager, FermentingBarrelRecipe recipe) {
        return String.format("<recipetype:sushigocrafting:fermenting_barrel>.addRecipe(%s, %s, %s, %s);", StringUtils.quoteAndEscape(recipe.getId()), IIngredient.fromIngredient(recipe.getInput()).getCommandString(), new MCFluidStack(recipe.getFluid()).getCommandString(), new MCItemStack(recipe.getOutput()).getCommandString());
    }
    
    @Override
    public Optional<Function<ResourceLocation, FermentingBarrelRecipe>> replaceIngredients(IRecipeManager manager, FermentingBarrelRecipe recipe, List<IReplacementRule> rules) {
        Optional<Ingredient> input = IRecipeHandler.attemptReplacing(recipe.getInput(), Ingredient.class, recipe, rules);
        if(!input.isPresent()){
            return Optional.empty();
        }
        return Optional.of(id -> new FermentingBarrelRecipe(id, input.orElseGet(recipe::getInput), recipe.getFluid(), recipe.getOutput()));
    }
    
    @Override
    public <U extends IRecipe<?>> boolean doesConflict(IRecipeManager manager, FermentingBarrelRecipe firstRecipe, U secondRecipe) {
        return IngredientHelper.canConflict(firstRecipe.getInput(), ((FermentingBarrelRecipe) secondRecipe).getInput());
    }
}
