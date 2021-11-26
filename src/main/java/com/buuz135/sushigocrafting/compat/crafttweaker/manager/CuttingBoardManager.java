package com.buuz135.sushigocrafting.compat.crafttweaker.manager;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.sushigocrafting.CuttingBoard")
public class CuttingBoardManager implements IRecipeManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IIngredient input, String foodOutput) {
        name = fixRecipeName(name);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, new CuttingBoardRecipe(new ResourceLocation("crafttweaker", name), input.asVanillaIngredient(), foodOutput)));
    }
    
    @Override
    public void removeRecipe(IIngredient output) {
        throw new RuntimeException("Cutting Board recipes can only be removed by name, not output!");
    }
    
    @Override
    public IRecipeType<CuttingBoardRecipe> getRecipeType() {
        return CuttingBoardRecipe.SERIALIZER.getRecipeType();
    }
}
