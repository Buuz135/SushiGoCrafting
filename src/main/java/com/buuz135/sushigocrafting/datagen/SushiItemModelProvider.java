package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.List;

public class SushiItemModelProvider extends ItemModelProvider {


    public SushiItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        List<Item> FILTER_ITEM = Arrays.asList(SushiContent.Items.WASABI_SEEDS.get(), SushiContent.Items.RICE_SEEDS.get(), SushiContent.Items.SOY_SEEDS.get(),
                SushiContent.Items.SESAME_SEEDS.get(), SushiContent.Items.CUCUMBER_SEEDS.get(), SushiContent.Items.AVOCADO_SAPLING.get());
        SushiContent.Items.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> item instanceof BlockItem && !FILTER_ITEM.contains(item)).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).getPath())));
        });
        SushiContent.Items.REGISTRY.getEntries().stream().map(DeferredHolder::get).filter(item -> !(item instanceof BlockItem) && !FILTER_ITEM.contains(item)).forEach(this::generateItem);
        FILTER_ITEM.forEach(this::generateItem);
    }

    private void generateItem(Item item) {
        if (item.equals(SushiContent.Items.SEAWEED_ON_A_STICK.get())) {
            getBuilder(BuiltInRegistries.ITEM.getKey(item).getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"))
                    .texture("layer0", modLoc("item/" + (item instanceof SushiItem ? (((SushiItem) item).getCategory().isEmpty() ? "" : ((SushiItem) item).getCategory() + "/") : "") + BuiltInRegistries.ITEM.getKey(item).getPath()));
        } else {
            getBuilder(BuiltInRegistries.ITEM.getKey(item).getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", modLoc("item/" + (item instanceof SushiItem ? (((SushiItem) item).getCategory().isEmpty() ? "" : ((SushiItem) item).getCategory() + "/") : "") + BuiltInRegistries.ITEM.getKey(item).getPath()));
        }
    }
}
