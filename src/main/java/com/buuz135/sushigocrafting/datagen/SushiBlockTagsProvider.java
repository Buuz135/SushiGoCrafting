package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


public class SushiBlockTagsProvider extends BlockTagsProvider {

    public SushiBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, SushiGoCrafting.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LOG.get());
        tag(BlockTags.LOGS).add(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get());
        tag(BlockTags.SAPLINGS).add(SushiContent.Blocks.AVOCADO_SAPLING.get());
        tag(BlockTags.LEAVES).add(SushiContent.Blocks.AVOCADO_LEAVES.get());
        for (CustomCropBlock block : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            tag(BlockTags.CROPS).add(block);
            tag(BlockTags.BEE_GROWABLES).add(block);
        }
        tag(BlockTags.MINEABLE_WITH_AXE).add(SushiContent.Blocks.AVOCADO_LOG.get(),
                SushiContent.Blocks.AVOCADO_LEAVES_LOG.get(),
                SushiContent.Blocks.ROLLER.get(),
                SushiContent.Blocks.CUTTING_BOARD.get(),
                SushiContent.Blocks.FERMENTATION_BARREL.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(SushiContent.Blocks.RICE_COOKER.get(), SushiContent.Blocks.COOLER_BOX.get());
    }
}
