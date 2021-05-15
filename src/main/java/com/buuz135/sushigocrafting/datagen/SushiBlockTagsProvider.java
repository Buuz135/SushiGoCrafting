package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
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
        getOrCreateBuilder(BlockTags.SAPLINGS).add(SushiContent.Blocks.AVOCADO_SAPLING.get());
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            getOrCreateBuilder(BlockTags.CROPS).add(block);
            getOrCreateBuilder(BlockTags.BEE_GROWABLES).add(block);
        }
    }
}
