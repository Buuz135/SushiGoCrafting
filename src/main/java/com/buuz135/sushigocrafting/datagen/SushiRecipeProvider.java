package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapelessRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public class SushiRecipeProvider extends RecipeProvider {

    public SushiRecipeProvider(DataGenerator generatorIn, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generatorIn.getPackOutput(), lookupProvider);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.KNIFE_CLEAVER.get())
                .pattern(" II").pattern("II ").pattern("S  ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.ROLLER.get())
                .pattern("BBB").pattern("SSS").pattern("BBB")
                .define('B', Items.BAMBOO)
                .define('S', Items.STRING)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.RICE_COOKER.get())
                .pattern("IGI").pattern("IFI").pattern("IRI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .define('F', Items.FURNACE)
                .define('R', Items.REDSTONE)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.COOLER_BOX.get())
                .pattern("IGI").pattern("IFI").pattern("III")
                .define('I', Blocks.SNOW_BLOCK)
                .define('G', Blocks.IRON_TRAPDOOR)
                .define('F', Tags.Items.CHESTS)
                .save(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.FERMENTATION_BARREL.get())
                .pattern("IGI").pattern("IFI").pattern("III")
                .define('I', ItemTags.PLANKS)
                .define('G', ItemTags.WOODEN_PRESSURE_PLATES)
                .define('F', Blocks.FURNACE)
                .save(consumer);
        //SimpleCookingRecipeBuilder.smelting(Ingredient.of(SushiContent.Items.SEAWEED.get()), RecipeCategory.FOOD, SushiContent.Items.DRY_SEAWEED.get(), 0.3f, 200).unlockedBy("has_seaweed", has(SushiContent.Items.SEAWEED.get())).save(consumer);
        //TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Blocks.SEAWEED_BLOCK.get()).requires(Ingredient.of(SushiContent.Items.DRY_SEAWEED.get()), 9).save(consumer);
        //TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.DRY_SEAWEED.get(), 9).requires(Ingredient.of(SushiContent.Blocks.SEAWEED_BLOCK.get()), 1).save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "seaweed_uncrafting"));
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.CUTTING_BOARD.get())
                .pattern("   ").pattern("SSS").pattern("BBB")
                .define('S', ItemTags.SLABS)
                .define('B', ItemTags.LOGS)
                .save(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.SEAWEED_ON_A_STICK.get()).requires(Items.FISHING_ROD).requires(Items.KELP).save(consumer);

        for (IFoodIngredient value : FoodAPI.get().getFoodIngredient()) {
            if (value instanceof FoodIngredient && ((FoodIngredient) value).needsChoppingRecipe()) {
                CuttingBoardRecipe recipe = new CuttingBoardRecipe(((FoodIngredient) value).getInput().get(), value.getName());
                recipe.save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "cutting_board/" + value.getName()));
            }
        }
        new FermentingBarrelRecipe(Ingredient.EMPTY, new FluidStack(NeoForgeMod.MILK.get(), 250), new ItemStack(SushiContent.Items.CHEESE.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "fermenting_barrel/cheese"));
        new FermentingBarrelRecipe(Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "crops/soy_bean"))), new FluidStack(Fluids.WATER, 250), new ItemStack(SushiContent.Items.SOY_SAUCE.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "fermenting_barrel/soy"));
    }
}
