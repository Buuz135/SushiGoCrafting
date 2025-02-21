package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CuttingBoardRecipe implements Recipe<CraftingInput> {

    public static final MapCodec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(CuttingBoardRecipe::getInput),
            Codec.STRING.fieldOf("ingredient").forGetter(CuttingBoardRecipe::getIngredient)
    ).apply(in, CuttingBoardRecipe::new));

    public Ingredient input;
    public String ingredient;

    public CuttingBoardRecipe() {

    }

    public CuttingBoardRecipe(Ingredient input, String ingredient) {
        this.input = input;
        this.ingredient = ingredient;
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
        return SushiContent.RecipeSerializers.CUTTING_BOARD.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SushiContent.RecipeTypes.CUTTING_BOARD.get();
    }

    public Ingredient getInput() {
        return input;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, this, null);
    }
}
