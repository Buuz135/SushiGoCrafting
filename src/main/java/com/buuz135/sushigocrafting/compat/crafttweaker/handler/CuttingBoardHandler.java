package com.buuz135.sushigocrafting.compat.crafttweaker.handler;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.helper.IngredientHelper;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@IRecipeHandler.For(CuttingBoardRecipe.class)
public class CuttingBoardHandler implements IRecipeHandler<CuttingBoardRecipe> {
    
    @Override
    public String dumpToCommandString(IRecipeManager manager, CuttingBoardRecipe recipe) {
        return String.format("<recipetype:sushigocrafting:cutting_board>.addRecipe(%s, %s, %s);", StringUtils.quoteAndEscape(recipe.getId()), IIngredient.fromIngredient(recipe.getInput()).getCommandString(), StringUtils.quoteAndEscape(recipe.ingredient));
    }
    
    @Override
    public Optional<Function<ResourceLocation, CuttingBoardRecipe>> replaceIngredients(IRecipeManager manager, CuttingBoardRecipe recipe, List<IReplacementRule> rules) {
        Optional<Ingredient> input = IRecipeHandler.attemptReplacing(recipe.getInput(), Ingredient.class, recipe, rules);
        if(!input.isPresent()){
            return Optional.empty();
        }
        return Optional.of(id -> new CuttingBoardRecipe(id, input.orElseGet(recipe::getInput), recipe.getIngredient()));
    }
    
    @Override
    public <U extends IRecipe<?>> boolean doesConflict(IRecipeManager manager, CuttingBoardRecipe firstRecipe, U secondRecipe) {
        return IngredientHelper.canConflict(firstRecipe.getInput(), ((CuttingBoardRecipe) secondRecipe).getInput());
    }
}
