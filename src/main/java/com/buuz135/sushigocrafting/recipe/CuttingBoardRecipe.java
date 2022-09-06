package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CuttingBoardRecipe extends SerializableRecipe {

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
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return (GenericSerializer<? extends SerializableRecipe>) SushiContent.RecipeSerializers.CUTTING_BOARD.get();
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
}
