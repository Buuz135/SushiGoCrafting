package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

public class SushiLangProvider extends LanguageProvider {

    public SushiLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen.getPackOutput(), modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.sushigocrafting", "Sushi Go Crafting");
        add("entity.sushigocrafting.shrimp", "Shrimp");
        add("entity.sushigocrafting.tuna", "Tuna");
        add("text.sushigocrafting.book.title", "Becoming an Itamae (Sushi Go Crafting Manual)");
        add("text.sushigocrafting.effects", "Effects");
        add("text.sushigocrafting.hunger", "Hunger");
        add("text.sushigocrafting.saturation", "Saturation");
        add("text.sushigocrafting.hold", "Hold");
        add("text.sushigocrafting.sushi_effect", " for sushi effect");
        add("text.sushigocrafting.perfect", "Perfect");
        add("text.sushigocrafting.weirdly_balanced", "Weirdly Balanced");
        add("text.sushigocrafting.almost_hollow", "Almost Hollow");
        add("text.sushigocrafting.overflowing", "Overflowing");
        add("text.sushigocrafting.add_food_effect", "Adds Food Effect");
        add("text.sushigocrafting.modify_food_effect", "Modifies Food Effect");
        add("text.sushigocrafting.increase_level_by", "Increases Level By");
        add("text.sushigocrafting.multiply_time_by", "Multiplies Time By");
        add("text.sushigocrafting.weight", "Weight");
        add("text.sushigocrafting.consumes", "Consomes");
        add("text.sushigocrafting.left_click_increase", "Left Click to Increase");
        add("text.sushigocrafting.right_click_decrease", "Right Click to Decrease");
        add("text.sushigocrafting.list_ingredients", "Lists all Sushi Go Crafting Food Ingredients");
        add("text.sushigocrafting.list_oof_ingredients", "List of all known Food Ingredients");
        add("text.sushigocrafting.food_ingredients", "Food Ingredients");
        add("text.sushigocrafting.amount", "Amount");
        add("text.sushigocrafting.discovered_a", "You have discovered a");
        add("text.sushigocrafting.perfect_weight", "new perfect weight!");
        add("text.sushigocrafting.make_one", "Left Click to make 1");
        add("text.sushigocrafting.make_64", "Right Click to make 64");
        add("text.sushigocrafting.roll", "Roll");
        add("text.sushigocrafting.button", "Button");
        SushiContent.Items.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            add(blockItem, WordUtils.capitalize(BuiltInRegistries.ITEM.getKey(blockItem).getPath().replaceAll("_", " ")));
        });
        SushiContent.Items.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> !(item instanceof BlockItem)).forEach(item -> {
            add(item, WordUtils.capitalize(BuiltInRegistries.ITEM.getKey(item).getPath().replaceAll("_", " ")));
        });
        SushiContent.Effects.REGISTRY.getEntries().stream().map(DeferredHolder::get).forEach(effect -> {
            add(effect, WordUtils.capitalize(BuiltInRegistries.MOB_EFFECT.getKey(effect).getPath().replaceAll("_", " ")));
        });

        addDesc(SushiContent.Effects.STEADY_HANDS, "Increases the amount you get from chopping on the cutting board");
        addDesc(SushiContent.Effects.ACQUIRED_TASTE, "Gives extra nutrition and saturation when eating food");
        addDesc(SushiContent.Effects.SMALL_BITES, "A chance to give you back the food you are eating");
    }

    private void addDesc(DeferredHolder<MobEffect, MobEffect> effect, String description) {
        add(effect.get().getDescriptionId() + ".description", description);
    }
}
