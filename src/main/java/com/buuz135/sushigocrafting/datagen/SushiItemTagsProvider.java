package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
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
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        getOrCreateBuilder(Tags.Items.SEEDS)
                .add(SushiContent.Items.RICE_SEEDS.get())
                .add(SushiContent.Items.CUCUMBER_SEEDS.get())
                .add(SushiContent.Items.SOY_SEEDS.get())
                .add(SushiContent.Items.WASABI_SEEDS.get())
                .add(SushiContent.Items.SESAME_SEEDS.get());
        getOrCreateBuilder(ItemTags.FISHES).add(SushiContent.Items.RAW_TUNA.get());
    }
}
