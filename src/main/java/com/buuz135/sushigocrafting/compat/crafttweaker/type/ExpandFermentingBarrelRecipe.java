package com.buuz135.sushigocrafting.compat.crafttweaker.type;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.fluid.MCFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@NativeTypeRegistration(value = FermentingBarrelRecipe.class, zenCodeName = "mods.sushigocrafting.FermentingBarrelRecipe")
public class ExpandFermentingBarrelRecipe {

    @ZenCodeType.Method
    @ZenCodeType.Getter("input")
    public static IIngredient getInput(FermentingBarrelRecipe internal) {
        return IIngredient.fromIngredient(internal.getInput());
    }

    @ZenCodeType.Method
    @ZenCodeType.Getter("fluid")
    public static IFluidStack getFluid(FermentingBarrelRecipe internal) {
        return new MCFluidStack(internal.getFluid());
    }

    @ZenCodeType.Method
    @ZenCodeType.Getter("output")
    public static IItemStack getOutput(FermentingBarrelRecipe internal) {
        return new MCItemStack(internal.getOutput());
    }
}
