package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FermentingBarrelRecipe implements Recipe<CraftingInput> {

    public static final MapCodec<FermentingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
            Ingredient.CODEC.optionalFieldOf("input").forGetter(fermentingBarrelRecipe -> fermentingBarrelRecipe.input),
            FluidStackTemplate.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(recipe -> recipe.output)
    ).apply(in, (ingredient, fluidStack, itemStack) -> new FermentingBarrelRecipe(ingredient, fluidStack, itemStack)));
    public static final StreamCodec<RegistryFriendlyByteBuf, FermentingBarrelRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, FermentingBarrelRecipe::getInput,
            FluidStackTemplate.STREAM_CODEC, recipe -> recipe.fluid,
            ItemStackTemplate.STREAM_CODEC, recipe -> recipe.output,
            FermentingBarrelRecipe::new);
    private final Optional<Ingredient> input;
    private final FluidStackTemplate fluid;
    private final ItemStackTemplate output;

    public FermentingBarrelRecipe(Optional<Ingredient> input, FluidStackTemplate stack, ItemStackTemplate output) {
        this.input = input;
        this.fluid = stack;
        this.output = output;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<FermentingBarrelRecipe> getSerializer() {
        return SushiContent.RecipeSerializers.FERMENTING_BARREL.get();
    }

    @Override
    public RecipeType<FermentingBarrelRecipe> getType() {
        return SushiContent.RecipeTypes.FERMENTING_BARREL.get();
    }

    public Optional<Ingredient> getInput() {
        return input;
    }

    public FluidStack getFluid() {
        return fluid.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public void save(RecipeOutput output, Identifier id) {
        output.accept(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, id), this, null);
    }
}
