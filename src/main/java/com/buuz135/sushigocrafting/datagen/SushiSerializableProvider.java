package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public class SushiSerializableProvider extends TitaniumSerializableProvider {


    public SushiSerializableProvider(DataGenerator generatorIn, String modid) {
        super(generatorIn, modid);
    }

    @Override
    public void add(Map<IJsonFile, IJSONGenerator> map) {
        for (IFoodIngredient value : FoodAPI.get().getFoodIngredient()) {
            if (value instanceof FoodIngredient && ((FoodIngredient) value).needsChoppingRecipe()) {
                CuttingBoardRecipe recipe = new CuttingBoardRecipe(new ResourceLocation(SushiGoCrafting.MOD_ID, value.getName()), ((FoodIngredient) value).getInput().get(), value.getName());
                map.put(recipe, recipe);
            }
        }
        new FermentingBarrelRecipe(new ResourceLocation(SushiGoCrafting.MOD_ID, "cheese"), Ingredient.EMPTY, new FluidStack(ForgeMod.MILK.get(), 250), new ItemStack(SushiContent.Items.CHEESE.get()));
        new FermentingBarrelRecipe(new ResourceLocation(SushiGoCrafting.MOD_ID, "soy"), Ingredient.fromTag(ItemTags.createOptional(new ResourceLocation("forge", "crops/soy_bean"))), new FluidStack(Fluids.WATER, 250), new ItemStack(SushiContent.Items.SOY_SAUCE.get()));
        FermentingBarrelRecipe.RECIPES.forEach(fermentingBarrelRecipe -> map.put(fermentingBarrelRecipe, fermentingBarrelRecipe));
    }
}
