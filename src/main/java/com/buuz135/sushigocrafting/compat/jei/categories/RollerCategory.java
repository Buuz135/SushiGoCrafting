package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.client.gui.provider.RollerAssetProvider;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
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
    public Component getTitle() {
        return new TranslatableComponent(SushiContent.Blocks.ROLLER.get().getDescriptionId());
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/gui/roller.png"), 8, 17, 160, 79);
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
        stackGroup.init(0, false, 4, 2);
        stackGroup.set(0, new ItemStack(recipe.stack));
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            if (!recipe.stack.getIngredientList().get(i).isEmpty()) {
                stackGroup.init(i + 1, true, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 8, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18);
                stackGroup.set(i + 1, new ItemStack(recipe.stack.getIngredientList().get(i).getItem()));
            }
        }
    }

    @Override
    public void draw(Recipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 5, 3, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, -7, -17, 0, 0, recipe.stack.getIngredientList().size(), recipe.stack.getType().getSlotPosition(), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.LIGHT_BLUE.getFireworkColor()), integer -> true);
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            RollerWeightSelectorButtonComponent.drawBackground(matrixStack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 8 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18);
            RollerWeightSelectorButtonComponent.drawForeground(matrixStack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 8 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18, Integer.MIN_VALUE, 18, recipe.stack.getType().getName(), i);
        }
    }

    public static class Recipe {

        private final FoodItem stack;

        public Recipe(FoodItem stack) {
            this.stack = stack;
        }
    }
}
