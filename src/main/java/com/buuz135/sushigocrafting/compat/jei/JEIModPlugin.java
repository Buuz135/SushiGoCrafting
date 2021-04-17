package com.buuz135.sushigocrafting.compat.jei;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.compat.jei.categories.CuttingBoardCategory;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.hrznstudio.titanium.util.RecipeUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {

    private CuttingBoardCategory cuttingBoardRecipe;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(this.cuttingBoardRecipe = new CuttingBoardCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeUtil.getRecipes(Minecraft.getInstance().world, CuttingBoardRecipe.SERIALIZER.getRecipeType()), cuttingBoardRecipe.getUid());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SushiContent.Blocks.CUTTING_BOARD.get()), cuttingBoardRecipe.getUid());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SushiGoCrafting.MOD_ID, "default");
    }
}
