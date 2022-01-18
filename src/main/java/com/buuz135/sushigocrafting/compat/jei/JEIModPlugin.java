package com.buuz135.sushigocrafting.compat.jei;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.compat.jei.categories.CuttingBoardCategory;
import com.buuz135.sushigocrafting.compat.jei.categories.FermentationBarrelCategory;
import com.buuz135.sushigocrafting.compat.jei.categories.RiceCookerCategory;
import com.buuz135.sushigocrafting.compat.jei.categories.RollerCategory;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.hrznstudio.titanium.util.RecipeUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {

    public static List<ItemStack> FUELS;
    private CuttingBoardCategory cuttingBoardRecipe;
    private RollerCategory rollerCategory;
    private RiceCookerCategory riceCookerCategory;
    private FermentationBarrelCategory fermentationBarrelCategory;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(this.cuttingBoardRecipe = new CuttingBoardCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(this.rollerCategory = new RollerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(this.riceCookerCategory = new RiceCookerCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(this.fermentationBarrelCategory = new FermentationBarrelCategory(registry.getJeiHelpers().getGuiHelper()));
        FUELS = ForgeRegistries.ITEMS.getValues().stream().filter(item -> FurnaceBlockEntity.isFuel(new ItemStack(item))).map(item -> new ItemStack(item)).collect(Collectors.toList());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Collections.singleton(new RiceCookerCategory.RiceCookerRecipe()), riceCookerCategory.getUid());
        registration.addRecipes(RecipeUtil.getRecipes(Minecraft.getInstance().level, CuttingBoardRecipe.SERIALIZER.getRecipeType()), cuttingBoardRecipe.getUid());
        registration.addRecipes(FoodHelper.REGISTERED.values().stream().flatMap(Collection::stream).map(RollerCategory.Recipe::new).collect(Collectors.toList()), rollerCategory.getUid());
        registration.addRecipes(RecipeUtil.getRecipes(Minecraft.getInstance().level, FermentingBarrelRecipe.SERIALIZER.getRecipeType()), fermentationBarrelCategory.getUid());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SushiContent.Blocks.CUTTING_BOARD.get()), cuttingBoardRecipe.getUid());
        registration.addRecipeCatalyst(new ItemStack(SushiContent.Blocks.ROLLER.get()), rollerCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(SushiContent.Blocks.RICE_COOKER.get()), riceCookerCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(SushiContent.Blocks.FERMENTATION_BARREL.get()), fermentationBarrelCategory.getUid());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SushiGoCrafting.MOD_ID, "default");
    }
}
