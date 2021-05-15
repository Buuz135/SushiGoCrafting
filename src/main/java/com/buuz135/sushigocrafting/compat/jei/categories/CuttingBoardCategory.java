package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.util.ItemStackUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class CuttingBoardCategory implements IRecipeCategory<CuttingBoardRecipe> {

    public static ResourceLocation UID = new ResourceLocation(SushiGoCrafting.MOD_ID, "cutting_board");

    private final IGuiHelper guiHelper;

    public CuttingBoardCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CuttingBoardRecipe> getRecipeClass() {
        return CuttingBoardRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent(SushiContent.Blocks.CUTTING_BOARD.get().getTranslationKey()).getString();
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
    public void setIngredients(CuttingBoardRecipe cuttingBoardRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(cuttingBoardRecipe.input.getMatchingStacks())));
        iIngredients.setOutput(VanillaTypes.ITEM, new ItemStack(FoodAPI.get().getIngredientFromName(cuttingBoardRecipe.ingredient).getItem()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CuttingBoardRecipe cuttingBoardRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup stackGroup = iRecipeLayout.getItemStacks();
        stackGroup.init(0, true, 30, 52);
        stackGroup.init(1, false, 30, 0);
        stackGroup.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
        stackGroup.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public void draw(CuttingBoardRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/gui/jei.png"));
        Minecraft.getInstance().currentScreen.blit(matrixStack, 31, 22, 0, 0, 15, 22);
        float scale = 4;
        //matrixStack.translate(0,0, 100);
        //matrixStack.translate(0,0, -200);
        matrixStack.scale(scale, scale, scale);
        ItemStackUtil.renderItemIntoGUI(matrixStack, new ItemStack(SushiContent.Blocks.CUTTING_BOARD.get()), 2, 4);
        matrixStack.scale(1 / scale, 1 / scale, 1 / scale);
        //matrixStack.translate(0,0, 200);

    }
}
