package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.util.ItemStackUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CuttingBoardCategory implements IRecipeCategory<CuttingBoardRecipe> {

    private final IGuiHelper guiHelper;

    public CuttingBoardCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }


    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return SushiRecipeTypes.CUTTING_BOARD;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(SushiContent.Blocks.CUTTING_BOARD.get().getDescriptionId());
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(80, 80);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CuttingBoardRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 52).addIngredients(recipe.input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 30, 0).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FoodAPI.get().getIngredientFromName(recipe.ingredient).getItem()));
    }


    @Override
    public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/gui/jei.png"), 31, 22, 0, 0, 15, 22);
        float scale = 4;
        //matrixStack.translate(0,0, 100);
        //matrixStack.translate(0,0, -200);
        guiGraphics.pose().scale(scale, scale, scale);
        ItemStackUtil.renderItemIntoGUI(guiGraphics.pose(), new ItemStack(SushiContent.Blocks.CUTTING_BOARD.get()), 2, 4);
        guiGraphics.pose().scale(1 / scale, 1 / scale, 1 / scale);
        //matrixStack.translate(0,0, 200);
    }

}
