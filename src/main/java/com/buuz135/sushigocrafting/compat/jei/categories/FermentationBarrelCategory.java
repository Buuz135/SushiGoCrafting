package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
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
import java.util.Arrays;

public class FermentationBarrelCategory implements IRecipeCategory<FermentingBarrelRecipe> {

    public static ResourceLocation UID = new ResourceLocation(SushiGoCrafting.MOD_ID, "fermentation_barrel");

    private final IGuiHelper guiHelper;
    private final IDrawable smallTank;

    public FermentationBarrelCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.smallTank = guiHelper.createDrawable(DefaultAssetProvider.DEFAULT_LOCATION, 235 + 3, 1 + 3, 12, 13);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FermentingBarrelRecipe> getRecipeClass() {
        return FermentingBarrelRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent(SushiContent.Blocks.FERMENTATION_BARREL.get().getTranslationKey()).getString();
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(101, 47);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(FermentingBarrelRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(recipe.input.getMatchingStacks())));
        iIngredients.setInput(VanillaTypes.FLUID, recipe.getFluid());
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, FermentingBarrelRecipe cuttingBoardRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup stackGroup = iRecipeLayout.getItemStacks();
        stackGroup.init(0, true, 3, 15);
        stackGroup.init(1, false, 80, 15);
        stackGroup.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
        stackGroup.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
        iRecipeLayout.getFluidStacks().init(0, true, 26 + 3, 15 + 3, 12, 13, 1000, false, smallTank);
        iRecipeLayout.getFluidStacks().set(0, iIngredients.getInputs(VanillaTypes.FLUID).get(0));
    }

    @Override
    public void draw(FermentingBarrelRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 4, 16, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.BLUE.getFireworkColor()), integer -> true);
        AssetUtil.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 26, 15);
        AssetUtil.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 51, 16);
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 81, 16, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
    }

}
