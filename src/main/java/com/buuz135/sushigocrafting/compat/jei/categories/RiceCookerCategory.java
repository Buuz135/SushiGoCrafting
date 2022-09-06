package com.buuz135.sushigocrafting.compat.jei.categories;

import com.buuz135.sushigocrafting.compat.jei.JEIModPlugin;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.tile.machinery.RiceCookerTile;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.addon.SlotsScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;

public class RiceCookerCategory implements IRecipeCategory<RiceCookerCategory.RiceCookerRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable smallTank;

    public RiceCookerCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.smallTank = guiHelper.createDrawable(DefaultAssetProvider.DEFAULT_LOCATION, 235 + 3, 1 + 3, 12, 13);
    }

    @Override
    public Component getTitle() {
        return Component.translatable(SushiContent.Blocks.RICE_COOKER.get().getDescriptionId());
    }

    @Override
    public RecipeType<RiceCookerRecipe> getRecipeType() {
        return SushiRecipeTypes.RICE_COOKER;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RiceCookerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 3).addIngredients(Ingredient.of(RiceCookerTile.RICE));
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 37).addIngredients(VanillaTypes.ITEM_STACK, JEIModPlugin.FUELS);
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 30).setFluidRenderer(2000, false, 12, 13).setOverlay(smallTank, 0, 0).addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(Fluids.WATER, 1000));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 16).addIngredient(VanillaTypes.ITEM_STACK, SushiContent.Items.COOKED_RICE.get().withAmount(50));
    }

    @Override
    public void draw(RiceCookerRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        SlotsScreenAddon.drawAsset(stack, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 4, 3, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.BLUE.getFireworkColor()), integer -> true);
        AssetUtil.drawAsset(stack, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.TANK_SMALL), 3, 27);
        AssetUtil.drawAsset(stack, Minecraft.getInstance().screen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 30, 16);
        SlotsScreenAddon.drawAsset(stack, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 60, 16, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.ORANGE.getFireworkColor()), integer -> true);
        SlotsScreenAddon.drawAsset(stack, Minecraft.getInstance().screen, DefaultAssetProvider.DEFAULT_PROVIDER, 30, 37, 0, 0, 1, integer -> Pair.of(0, 0), integer -> ItemStack.EMPTY, true, integer -> new Color(DyeColor.RED.getFireworkColor()), integer -> true);

    }


    public static class RiceCookerRecipe {

    }
}
