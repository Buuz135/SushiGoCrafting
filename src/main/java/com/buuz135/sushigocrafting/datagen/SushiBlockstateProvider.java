package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SushiBlockstateProvider extends BlockStateProvider {

    public SushiBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        crop((CropsBlock) SushiContent.Blocks.CUCUMBER_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.RICE_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.WASABI_CROP.get());
        crop((CropsBlock) SushiContent.Blocks.SOY_CROP.get());
        simpleBlockUn(SushiContent.Blocks.SEAWEED.get());
        simpleBlockUn(SushiContent.Blocks.SEAWEED_PLANT.get());
        simpleBlockUn(SushiContent.Blocks.ROLLER.get());
        simpleBlockUn(SushiContent.Blocks.AVOCADO_SAPLING.get());
        horizontalBlock(SushiContent.Blocks.RICE_COOKER.get());
        simpleBlockUn(SushiContent.Blocks.AVOCADO_LOG.get());
        simpleBlockUn(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get());
        simpleBlockUn(SushiContent.Blocks.SEAWEED_BLOCK.get());
        getVariantBuilder(SushiContent.Blocks.AVOCADO_LEAVES.get())
                .partialState()
                .with(AvocadoLeavesBlock.STAGE, 0).addModels(
                ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + SushiContent.Blocks.AVOCADO_LEAVES.get().getRegistryName().getPath() + "_0"))).build())
                .partialState().with(AvocadoLeavesBlock.STAGE, 1).addModels(
                ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + SushiContent.Blocks.AVOCADO_LEAVES.get().getRegistryName().getPath() + "_1"))).build())
                .partialState().with(AvocadoLeavesBlock.STAGE, 2).addModels(
                ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + SushiContent.Blocks.AVOCADO_LEAVES.get().getRegistryName().getPath() + "_2"))).build());
    }

    private void crop(CropsBlock block) {
        getVariantBuilder(block).forAllStates(blockState -> {
            int age = blockState.get(block.getAgeProperty());
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath() + "_" + age))).build();
        });
    }

    private void simpleBlockUn(Block block) {
        simpleBlock(block, new ModelFile.UncheckedModelFile(modLoc("block/" + block.getRegistryName().getPath())));
    }

    private void horizontalBlock(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(block.getRegistryName());
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(file)
                        .rotationY(((int) state.get(RotatableBlock.FACING_HORIZONTAL).getHorizontalAngle()) % 360)
                        .build()
                );
    }

}
