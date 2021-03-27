package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.recipe.generator.TitaniumSerializableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class SushiRecipeProvider extends TitaniumSerializableProvider {


    public SushiRecipeProvider(DataGenerator generatorIn, String modid) {
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
    }
}
