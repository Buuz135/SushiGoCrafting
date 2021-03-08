package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collections;

public class SushiItemModelProvider extends ItemModelProvider {

    public SushiItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, new ExistingFileHelper(Collections.emptyList(), false));
    }

    @Override
    protected void registerModels() {
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof BlockItem).map(item -> (BlockItem) item).forEach(blockItem -> {
            getBuilder(blockItem.getBlock().getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile(blockItem.getBlock().getRegistryName()));
        });
        SushiContent.Items.REGISTRY.getEntries().stream().map(RegistryObject::get).filter(item -> !(item instanceof BlockItem)).forEach(item -> {
            getBuilder(item.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", modLoc("item/" + (item instanceof SushiItem ? (((SushiItem) item).getCategory().isEmpty() ? "" : ((SushiItem) item).getCategory() + "/") : "") + item.getRegistryName().getPath()));
        });
    }

}
