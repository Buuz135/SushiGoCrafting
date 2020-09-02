package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

public class SushiBlockstateProvider extends BlockStateProvider {

    public SushiBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        crop((CropsBlock) SushiContent.Blocks.CUCUMBER_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.RICE_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.HORSERADISH_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.SOY_CROP.get());
        simpleBlock(SushiContent.Blocks.SEAWEED.get(), new ModelFile.UncheckedModelFile(SushiContent.Blocks.SEAWEED.get().getRegistryName()));
        simpleBlock(SushiContent.Blocks.SEAWEED_PLANT.get(), new ModelFile.UncheckedModelFile(SushiContent.Blocks.SEAWEED_PLANT.get().getRegistryName()));
    }

    private void crop(CropsBlock block) {
        getVariantBuilder(block).forAllStates(blockState -> {
            int age = blockState.get(block.getAgeProperty());
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(block.getRegistryName().toString() + "_" + age))).build();
        });
    }

}
