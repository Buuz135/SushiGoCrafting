package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;

public class SushiLangProvider extends LanguageProvider {

    public SushiLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.sushigocrafting", "Sushi Go Crafting");
        add("entity.sushigocrafting.shrimp", "Shrimp");
        add("entity.sushigocrafting.tuna", "Tuna");
        add("text.sushigocrafting.book.title", "Becoming an Itamae (Sushi Go Crafting Manual)");
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            add(blockItem, WordUtils.capitalize(blockItem.getRegistryName().getPath().replaceAll("_", " ")));
        });
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> !(item instanceof BlockItem)).forEach(item -> {
            add(item, WordUtils.capitalize(item.getRegistryName().getPath().replaceAll("_", " ")));
        });
        SushiContent.Effects.REGISTRY.getEntries().stream().map(RegistryObject::get).forEach(effect -> {
            add(effect, WordUtils.capitalize(effect.getRegistryName().getPath().replaceAll("_", " ")));
        });
    }
}
