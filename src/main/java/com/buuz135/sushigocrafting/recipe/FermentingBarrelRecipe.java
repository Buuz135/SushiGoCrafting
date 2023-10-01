package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FermentingBarrelRecipe extends SerializableRecipe {

    public static List<FermentingBarrelRecipe> RECIPES = new ArrayList<>();

    public Ingredient input = Ingredient.EMPTY;
    public FluidStack fluid = FluidStack.EMPTY;
    public ItemStack output;

    public FermentingBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public FermentingBarrelRecipe(ResourceLocation resourceLocation, Ingredient input, FluidStack stack, ItemStack output) {
        super(resourceLocation);
        this.input = input;
        this.fluid = stack;
        this.output = output;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return (GenericSerializer<? extends SerializableRecipe>) SushiContent.RecipeSerializers.FERMENTING_BARREL.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SushiContent.RecipeTypes.FERMENTING_BARREL.get();
    }

    public Ingredient getInput() {
        return input;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public ItemStack getOutput() {
        return output;
    }
}
