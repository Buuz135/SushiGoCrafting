package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SushiItemModelProvider extends ItemModelProvider {


    public SushiItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, new ExistingFileHelper(Collections.emptyList(), false));
    }

    @Override
    protected void registerModels() {
        List<Item> FILTER_ITEM = Arrays.asList(SushiContent.Items.SEAWEED.get(), SushiContent.Items.WASABI_SEEDS.get(), SushiContent.Items.RICE_SEEDS.get(), SushiContent.Items.SOY_SEEDS.get(),
                SushiContent.Items.SESAME_SEEDS.get(), SushiContent.Items.CUCUMBER_SEEDS.get());
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem && !FILTER_ITEM.contains(item)).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(blockItem.getBlock().getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + blockItem.getBlock().getRegistryName().getPath())));
        });
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> !(item instanceof BlockItem) && !FILTER_ITEM.contains(item)).forEach(this::generateItem);
        FILTER_ITEM.forEach(this::generateItem);
    }

    private void generateItem(Item item) {
        getBuilder(item.getRegistryName().getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + (item instanceof SushiItem ? (((SushiItem) item).getCategory().isEmpty() ? "" : ((SushiItem) item).getCategory() + "/") : "") + item.getRegistryName().getPath()));
    }
}
