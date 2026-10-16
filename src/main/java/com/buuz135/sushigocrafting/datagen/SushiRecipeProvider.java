package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.FoodIngredient;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.recipe.FermentingBarrelRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStackTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.Optional;

public class SushiRecipeProvider extends RecipeProvider {

    protected SushiRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        RecipeOutput consumer = output;
        shaped(RecipeCategory.MISC, SushiContent.Items.KNIFE_CLEAVER.get()).unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .pattern(" II").pattern("II ").pattern("S  ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .save(consumer);
        shaped(RecipeCategory.MISC, SushiContent.Items.ROLLER.get()).unlockedBy("has_item", has(Items.BAMBOO))
                .pattern("BBB").pattern("SSS").pattern("BBB")
                .define('B', Items.BAMBOO)
                .define('S', Items.STRING)
                .save(consumer);
        shaped(RecipeCategory.MISC, SushiContent.Items.RICE_COOKER.get()).unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .pattern("IGI").pattern("IFI").pattern("IRI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .define('F', Items.FURNACE)
                .define('R', Items.REDSTONE)
                .save(consumer);
        shaped(RecipeCategory.MISC, SushiContent.Items.COOLER_BOX.get()).unlockedBy("has_item", has(Blocks.SNOW_BLOCK))
                .pattern("IGI").pattern("IFI").pattern("III")
                .define('I', Blocks.SNOW_BLOCK)
                .define('G', Blocks.IRON_TRAPDOOR)
                .define('F', Tags.Items.CHESTS)
                .save(consumer);
        shaped(RecipeCategory.MISC, SushiContent.Items.FERMENTATION_BARREL.get()).unlockedBy("has_item", has(ItemTags.PLANKS))
                .pattern("IGI").pattern("IFI").pattern("III")
                .define('I', ItemTags.PLANKS)
                .define('G', ItemTags.WOODEN_PRESSURE_PLATES)
                .define('F', Blocks.FURNACE)
                .save(consumer);
        //SimpleCookingRecipeBuilder.smelting(Ingredient.of(SushiContent.Items.SEAWEED.get()), RecipeCategory.FOOD, SushiContent.Items.DRY_SEAWEED.get(), 0.3f, 200).unlockedBy("has_seaweed", has(SushiContent.Items.SEAWEED.get())).save(consumer);
        //shapeless(RecipeCategory.MISC, SushiContent.Blocks.SEAWEED_BLOCK.get()).requires(Ingredient.of(SushiContent.Items.DRY_SEAWEED.get()), 9).save(consumer);
        //shapeless(RecipeCategory.MISC, SushiContent.Items.DRY_SEAWEED.get(), 9).requires(Ingredient.of(SushiContent.Blocks.SEAWEED_BLOCK.get()), 1).save(consumer, Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "seaweed_uncrafting"));
        shaped(RecipeCategory.MISC, SushiContent.Items.CUTTING_BOARD.get()).unlockedBy("has_item", has(ItemTags.SLABS))
                .pattern("   ").pattern("SSS").pattern("BBB")
                .define('S', ItemTags.SLABS)
                .define('B', ItemTags.LOGS)
                .save(consumer);
        shapeless(RecipeCategory.MISC, SushiContent.Items.SEAWEED_ON_A_STICK.get()).unlockedBy("has_item", has(Items.FISHING_ROD)).requires(Items.FISHING_ROD).requires(Items.KELP).save(consumer);

        for (IFoodIngredient value : FoodAPI.get().getFoodIngredient()) {
            if (value instanceof FoodIngredient && ((FoodIngredient) value).needsChoppingRecipe()) {
                CuttingBoardRecipe recipe = new CuttingBoardRecipe(((FoodIngredient) value).getInput(items), value.getName());
                recipe.save(consumer, Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "cutting_board/" + value.getName()));
            }
        }
        new FermentingBarrelRecipe(Ingredient.EMPTY, new FluidStack(NeoForgeMod.MILK.get(), 250), new ItemStack(SushiContent.Items.CHEESE.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "fermenting_barrel/cheese"));
        new FermentingBarrelRecipe(Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "crops/soybean"))), new FluidStack(Fluids.WATER, 250), new ItemStack(SushiContent.Items.SOY_SAUCE.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "fermenting_barrel/soy"));
    }
}
