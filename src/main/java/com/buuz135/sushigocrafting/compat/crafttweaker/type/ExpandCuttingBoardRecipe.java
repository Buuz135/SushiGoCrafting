package com.buuz135.sushigocrafting.compat.crafttweaker.type;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@NativeTypeRegistration(value = CuttingBoardRecipe.class, zenCodeName = "mods.sushigocrafting.CuttingBoardRecipe")
public class ExpandCuttingBoardRecipe {

    @ZenCodeType.Method
    @ZenCodeType.Getter("input")
    public static IIngredient getInput(CuttingBoardRecipe internal) {
        return IIngredient.fromIngredient(internal.getInput());
    }

    @ZenCodeType.Method
    @ZenCodeType.Getter("ingredient")
    public static String getIngredient(CuttingBoardRecipe internal) {
        return internal.getIngredient();
    }
}
