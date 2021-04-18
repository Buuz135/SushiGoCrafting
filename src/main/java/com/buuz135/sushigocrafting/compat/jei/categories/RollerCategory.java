package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.client.gui.provider.RollerAssetProvider;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.stream.Collectors;

import static com.buuz135.sushigocrafting.compat.jei.categories.RollerCategory.Recipe;

public class RollerCategory implements IRecipeCategory<Recipe> {

    public static ResourceLocation UID = new ResourceLocation(SushiGoCrafting.MOD_ID, "roller");

    private final IGuiHelper guiHelper;

    public RollerCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent(SushiContent.Blocks.ROLLER.get().getTranslationKey()).getString();
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(120, 80);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(Recipe recipe, IIngredients iIngredients) {
        iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.stack));
        iIngredients.setInputs(VanillaTypes.ITEM, recipe.stack.getIngredientList().stream().filter(iFoodIngredient -> !iFoodIngredient.isEmpty()).map(iFoodIngredient -> new ItemStack(iFoodIngredient.getItem())).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients iIngredients) {
        IGuiItemStackGroup stackGroup = iRecipeLayout.getItemStacks();
        stackGroup.init(0, false, 0, 0);
        stackGroup.set(0, new ItemStack(recipe.stack));
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            if (!recipe.stack.getIngredientList().get(i).isEmpty()) {
                stackGroup.init(i + 1, true, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 20, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18);
                stackGroup.set(i + 1, new ItemStack(recipe.stack.getIngredientList().get(i).getItem()));
            }
        }
    }

    @Override
    public void draw(Recipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 1, 1, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, -19, -17, 0, 0, recipe.stack.getIngredientList().size(), recipe.stack.getType().getSlotPosition(), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.LIGHT_BLUE.getFireworkColor()), integer -> true);
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            RollerWeightSelectorButtonComponent.drawBackground(matrixStack, Minecraft.getInstance().currentScreen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 20 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18);
            RollerWeightSelectorButtonComponent.drawForeground(matrixStack, Minecraft.getInstance().currentScreen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 20 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18, Integer.MIN_VALUE, 18, recipe.stack.getType().getName(), i);
        }
    }

    public static class Recipe {

        private final FoodItem stack;

        public Recipe(FoodItem stack) {
            this.stack = stack;
            System.out.println(this.stack.getRegistryName());
        }
    }
}
