package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.gui.RollerWeightSelectorButtonComponent;
import com.buuz135.sushigocrafting.client.gui.provider.RollerAssetProvider;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;

import static com.buuz135.sushigocrafting.compat.jei.categories.RollerCategory.Recipe;

public class RollerCategory implements IRecipeCategory<Recipe> {

    private final IGuiHelper guiHelper;

    public RollerCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return SushiRecipeTypes.ROLLER;
    }

    @Override
    public ResourceLocation getUid() {
        return SushiRecipeTypes.ROLLER.getUid();
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return SushiRecipeTypes.ROLLER.getRecipeClass();
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
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            if (!recipe.stack.getIngredientList().get(i).isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 7, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 17).addIngredient(VanillaTypes.ITEM, new ItemStack(recipe.stack.getIngredientList().get(i).getItem()));
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 4, 2).addIngredient(VanillaTypes.ITEM, new ItemStack(recipe.stack));
    }


    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(stack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 5, 3, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
        SlotsScreenAddon.drawAsset(stack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, -7, -17, 0, 0, recipe.stack.getIngredientList().size(), recipe.stack.getType().getSlotPosition(), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.LIGHT_BLUE.getFireworkColor()), integer -> true);
        for (int i = 0; i < recipe.stack.getIngredientList().size(); i++) {
            RollerWeightSelectorButtonComponent.drawBackground(stack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 8 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18);
            RollerWeightSelectorButtonComponent.drawForeground(stack, Minecraft.getInstance().screen, RollerAssetProvider.INSTANCE, 0, 0, recipe.stack.getType().getSlotPosition().apply(i).getLeft() - 8 + 18, recipe.stack.getType().getSlotPosition().apply(i).getRight() - 18, Integer.MIN_VALUE, 18, recipe.stack.getType().getName(), i);
        }
    }

    public static class Recipe {

        private final FoodItem stack;

        public Recipe(FoodItem stack) {
            this.stack = stack;
        }
    }
}
