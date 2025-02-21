package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FermentingBarrelRecipe implements Recipe<CraftingInput> {

    public static final MapCodec<FermentingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
            Ingredient.CODEC.optionalFieldOf("input").forGetter(fermentingBarrelRecipe -> Optional.of(fermentingBarrelRecipe.input)),
            FluidStack.CODEC.fieldOf("fluid").forGetter(FermentingBarrelRecipe::getFluid),
            ItemStack.CODEC.fieldOf("output").forGetter(FermentingBarrelRecipe::getOutput)
    ).apply(in, (ingredient, fluidStack, itemStack) -> new FermentingBarrelRecipe(ingredient.orElse(Ingredient.EMPTY), fluidStack, itemStack)));
    public static List<FermentingBarrelRecipe> RECIPES = new ArrayList<>();
    public Ingredient input = Ingredient.EMPTY;
    public FluidStack fluid = FluidStack.EMPTY;
    public ItemStack output;

    public FermentingBarrelRecipe() {

    }

    public FermentingBarrelRecipe(Ingredient input, FluidStack stack, ItemStack output) {
        this.input = input;
        this.fluid = stack;
        this.output = output;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SushiContent.RecipeSerializers.FERMENTING_BARREL.get();
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

    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, this, null);
    }
}
