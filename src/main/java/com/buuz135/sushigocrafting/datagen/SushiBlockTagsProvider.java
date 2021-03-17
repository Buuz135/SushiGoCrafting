package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;


public class SushiBlockTagsProvider extends ForgeBlockTagsProvider {

    public SushiBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, existingFileHelper);
    }

    @Override
    public void registerTags() {
        getOrCreateBuilder(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LOG.get());
        getOrCreateBuilder(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get());
    }
}
