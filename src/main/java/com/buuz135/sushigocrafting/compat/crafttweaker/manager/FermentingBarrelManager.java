package com.buuz135.sushigocrafting.compat.crafttweaker.manager;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.sushigocrafting.FermentingBarrel")
public class FermentingBarrelManager implements IRecipeManager<FermentingBarrelRecipe> {

    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient input, IFluidStack stack, IItemStack output) {
        name = fixRecipeName(name);
        CraftTweakerAPI.apply(new ActionAddRecipe<>(this, new FermentingBarrelRecipe(new ResourceLocation("crafttweaker", name), input.asVanillaIngredient(), stack.getInternal(), output.getInternal())));
    }

    @Override
    public void remove(IIngredient output) {
        throw new RuntimeException("Fermenting Barrel recipes can only be removed by name, not output!");
    }

    @Override
    public RecipeType<FermentingBarrelRecipe> getRecipeType() {
        return (RecipeType<FermentingBarrelRecipe>)SushiContent.RecipeTypes.FERMENTING_BARREL.get();
    }
}
