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
