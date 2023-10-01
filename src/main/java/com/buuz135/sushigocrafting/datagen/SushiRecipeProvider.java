package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapelessRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SushiRecipeProvider extends TitaniumRecipeProvider {

    public SushiRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void register(Consumer<FinishedRecipe> consumer) {
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
        //TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.DRY_SEAWEED.get(), 9).requires(Ingredient.of(SushiContent.Blocks.SEAWEED_BLOCK.get()), 1).save(consumer, new ResourceLocation(SushiGoCrafting.MOD_ID, "seaweed_uncrafting"));
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.CUTTING_BOARD.get())
                .pattern("   ").pattern("SSS").pattern("BBB")
                .define('S', ItemTags.SLABS)
                .define('B', ItemTags.LOGS)
                .save(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.SEAWEED_ON_A_STICK.get()).requires(Items.FISHING_ROD).requires(Items.KELP).save(consumer);
    }
}
