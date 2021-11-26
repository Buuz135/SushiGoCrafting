package com.buuz135.sushigocrafting.compat.crafttweaker.manager;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.sushigocrafting.FermentingBarrel")
public class FermentingBarrelManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient input, IFluidStack stack, IItemStack output) {
        name = fixRecipeName(name);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, new FermentingBarrelRecipe(new ResourceLocation("crafttweaker", name), input.asVanillaIngredient(), stack.getInternal(), output.getInternal())));
    }
    
    @Override
    public void removeRecipe(IIngredient output) {
        throw new RuntimeException("Fermenting Barrel recipes can only be removed by name, not output!");
    }
    
    @Override
    public IRecipeType<FermentingBarrelRecipe> getRecipeType() {
        return FermentingBarrelRecipe.SERIALIZER.getRecipeType();
    }
}
