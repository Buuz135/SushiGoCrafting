package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;


public class SushiBlockTagsProvider extends BlockTagsProvider {

    public SushiBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, SushiGoCrafting.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        tag(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LOG.get());
        tag(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get());
        tag(BlockTags.SAPLINGS).add(SushiContent.Blocks.AVOCADO_SAPLING.get());
        tag(BlockTags.LEAVES).add(SushiContent.Blocks.AVOCADO_LEAVES.get());
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            tag(BlockTags.CROPS).add(block);
            tag(BlockTags.BEE_GROWABLES).add(block);
        }
    }
}
