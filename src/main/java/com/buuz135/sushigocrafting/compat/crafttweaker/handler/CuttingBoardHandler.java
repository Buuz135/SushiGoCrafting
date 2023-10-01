package com.buuz135.sushigocrafting.compat.crafttweaker.handler;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.IngredientUtil;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

@IRecipeHandler.For(CuttingBoardRecipe.class)
public class CuttingBoardHandler implements IRecipeHandler<CuttingBoardRecipe> {

    @Override
    public String dumpToCommandString(IRecipeManager manager, CuttingBoardRecipe recipe) {
        return String.format("<recipetype:sushigocrafting:cutting_board>.addRecipe(%s, %s, %s);", StringUtil.quoteAndEscape(recipe.getId()), IIngredient.fromIngredient(recipe.getInput()).getCommandString(), StringUtil.quoteAndEscape(recipe.ingredient));
    }

    /*@Override
    public Optional<Function<ResourceLocation, CuttingBoardRecipe>> replaceIngredients(IRecipeManager manager, CuttingBoardRecipe recipe, List<IReplacementRule> rules) {
        Optional<Ingredient> input = IRecipeHandler.attemptReplacing(recipe.getInput(), Ingredient.class, recipe, rules);
        if (input.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(id -> new CuttingBoardRecipe(id, input.orElseGet(recipe::getInput), recipe.getIngredient()));
    }*/


    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe firstRecipe, U secondRecipe) {
        return IngredientUtil.canConflict(firstRecipe.getInput(), ((CuttingBoardRecipe) secondRecipe).getInput());
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<CuttingBoardRecipe> recompose(IRecipeManager<? super CuttingBoardRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}
