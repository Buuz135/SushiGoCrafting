package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.compat.jei.JEIModPlugin;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
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
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.Arrays;

public class RiceCookerCategory implements IRecipeCategory<RiceCookerCategory.RiceCookerRecipe> {

    public static ResourceLocation UID = new ResourceLocation(SushiGoCrafting.MOD_ID, "rice_cooker");

    private final IGuiHelper guiHelper;
    private final IDrawable smallTank;

    public RiceCookerCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.smallTank = guiHelper.createDrawable(DefaultAssetProvider.DEFAULT_LOCATION, 235 + 3, 1 + 3, 12, 13);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends RiceCookerRecipe> getRecipeClass() {
        return RiceCookerRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent(SushiContent.Blocks.RICE_COOKER.get().getTranslationKey()).getString();
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(80, 56);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(RiceCookerRecipe cuttingBoardRecipe, IIngredients iIngredients) {
        iIngredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Arrays.asList(Ingredient.fromTag(RiceCookerTile.RICE).getMatchingStacks())));
        iIngredients.setInput(VanillaTypes.FLUID, new FluidStack(Fluids.WATER, 1000));
        iIngredients.setOutput(VanillaTypes.ITEM, SushiContent.Items.COOKED_RICE.get().withAmount(50));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, RiceCookerRecipe cuttingBoardRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup stackGroup = iRecipeLayout.getItemStacks();
        stackGroup.init(0, true, 3, 2);
        stackGroup.init(1, false, 59, 15);
        stackGroup.init(2, true, 29, 36);
        stackGroup.set(0, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
        stackGroup.set(1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
        stackGroup.set(2, JEIModPlugin.FUELS);
        iRecipeLayout.getFluidStacks().init(0, true, 6, 30, 12, 13, 2000, false, smallTank);
        iRecipeLayout.getFluidStacks().set(0, iIngredients.getInputs(VanillaTypes.FLUID).get(0));
    }

    @Override
    public void draw(RiceCookerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 4, 3, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.BLUE.getFireworkColor()), integer -> true);
        AssetUtil.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 3, 27);
        AssetUtil.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 30, 16);
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 60, 16, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
        SlotsScreenAddon.drawAsset(matrixStack, Minecraft.getInstance().currentScreen, DefaultAssetProvider.DEFAULT_PROVIDER, 30, 37, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.RED.getFireworkColor()), integer -> true);
    }

    public static class RiceCookerRecipe {

    }
}
