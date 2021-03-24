package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CuttingBoardRecipe extends SerializableRecipe {

    public static GenericSerializer<CuttingBoardRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(SushiGoCrafting.MOD_ID, "cutting_board"), CuttingBoardRecipe.class);
    public Ingredient input;
    public String ingredient;

    public CuttingBoardRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public CuttingBoardRecipe(ResourceLocation resourceLocation, Ingredient input, String ingredient) {
        super(resourceLocation);
        this.input = input;
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }

    public Ingredient getInput() {
        return input;
    }

    public String getIngredient() {
        return ingredient;
    }
}
