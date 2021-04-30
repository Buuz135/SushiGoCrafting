package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.recipe.generator.TitaniumRecipeProvider;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapedRecipeBuilder;
import com.hrznstudio.titanium.recipe.generator.TitaniumShapelessRecipeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SushiRecipeProvider extends TitaniumRecipeProvider {

    public SushiRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void register(Consumer<IFinishedRecipe> consumer) {
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.KNIFE_CLEAVER.get())
                .patternLine(" II").patternLine("II ").patternLine("S  ")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', Items.STICK)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.ROLLER.get())
                .patternLine("BBB").patternLine("SSS").patternLine("BBB")
                .key('B', Items.BAMBOO)
                .key('S', Items.STRING)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.RICE_COOKER.get())
                .patternLine("IGI").patternLine("IFI").patternLine("IRI")
                .key('I', Tags.Items.INGOTS_IRON)
                .key('G', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .key('F', Items.FURNACE)
                .key('R', Items.REDSTONE)
                .build(consumer);
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.COOLER_BOX.get())
                .patternLine("IGI").patternLine("IFI").patternLine("III")
                .key('I', Blocks.SNOW_BLOCK)
                .key('G', Blocks.IRON_TRAPDOOR)
                .key('F', Tags.Items.CHESTS)
                .build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(SushiContent.Items.SEAWEED.get()), SushiContent.Items.DRY_SEAWEED.get(), 0.3f, 200).addCriterion("has_seaweed", hasItem(SushiContent.Items.SEAWEED.get())).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(Items.MILK_BUCKET), SushiContent.Items.CHEESE.get(), 0.3f, 200).addCriterion("has_milk", hasItem(Items.MILK_BUCKET)).build(consumer);
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(SushiContent.Items.SOY_BEAN.get()), SushiContent.Items.SOY_SAUCE.get(), 0.3f, 200).addCriterion("has_soy", hasItem(SushiContent.Items.SOY_BEAN.get())).build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Blocks.SEAWEED_BLOCK.get()).addIngredient(Ingredient.fromItems(SushiContent.Items.DRY_SEAWEED.get()), 9).build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.DRY_SEAWEED.get(), 9).addIngredient(Ingredient.fromItems(SushiContent.Blocks.SEAWEED_BLOCK.get()), 1).build(consumer, new ResourceLocation(SushiGoCrafting.MOD_ID, "seaweed_uncrafting"));
        TitaniumShapedRecipeBuilder.shapedRecipe(SushiContent.Items.CUTTING_BOARD.get())
                .patternLine("   ").patternLine("SSS").patternLine("BBB")
                .key('S', ItemTags.SLABS)
                .key('B', ItemTags.LOGS)
                .build(consumer);
        TitaniumShapelessRecipeBuilder.shapelessRecipe(SushiContent.Items.SEAWEED_ON_A_STICK.get()).addIngredient(Items.FISHING_ROD).addIngredient(SushiContent.Items.SEAWEED.get()).build(consumer);
    }
}
