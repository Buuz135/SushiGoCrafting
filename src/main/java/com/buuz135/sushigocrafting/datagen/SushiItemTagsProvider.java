package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;


public class SushiItemTagsProvider extends ForgeItemTagsProvider {

    public SushiItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagsProvider, existingFileHelper);
    }

    @Override
    public void registerTags() {
        //super.registerTags();
        this.copy(BlockTags.LOGS, ItemTags.LOGS);
    }
}
