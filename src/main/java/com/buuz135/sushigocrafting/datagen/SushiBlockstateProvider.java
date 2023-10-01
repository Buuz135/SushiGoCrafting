package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.AvocadoLeavesBlock;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.block.RotatableBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class SushiBlockstateProvider extends BlockStateProvider {

    public SushiBlockstateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        crop(SushiContent.Blocks.CUCUMBER_CROP.get());
        crop(SushiContent.Blocks.RICE_CROP.get());
        crop(SushiContent.Blocks.WASABI_CROP.get());
        crop(SushiContent.Blocks.SOY_CROP.get());
        crop(SushiContent.Blocks.SESAME_CROP.get());
        //simpleBlockUn(SushiContent.Blocks.SEAWEED.get());
        //simpleBlockUn(SushiContent.Blocks.SEAWEED_PLANT.get());
        simpleBlockUn(SushiContent.Blocks.AVOCADO_SAPLING.get());
        horizontalBlock(SushiContent.Blocks.RICE_COOKER.get());
        logBlockRot(SushiContent.Blocks.AVOCADO_LOG.get());
        logBlockRot(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get());
        getVariantBuilder(SushiContent.Blocks.AVOCADO_LEAVES.get())
                .partialState()
                .with(AvocadoLeavesBlock.STAGE, 0).addModels(
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(SushiContent.Blocks.AVOCADO_LEAVES.get()).getPath() + "_0"))).build())
                .partialState().with(AvocadoLeavesBlock.STAGE, 1).addModels(
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(SushiContent.Blocks.AVOCADO_LEAVES.get()).getPath() + "_1"))).build())
                .partialState().with(AvocadoLeavesBlock.STAGE, 2).addModels(
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(SushiContent.Blocks.AVOCADO_LEAVES.get()).getPath() + "_2"))).build());
        horizontalBlock(SushiContent.Blocks.CUTTING_BOARD.get());
        horizontalBlock(SushiContent.Blocks.ROLLER.get());
        simpleBlockUn(SushiContent.Blocks.FERMENTATION_BARREL.get());
    }

    private void crop(CropBlock block) {
        getVariantBuilder(block).forAllStates(blockState -> {
            int age = blockState.getValue(CustomCropBlock.AGE);
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_" + age))).build();
        });
    }

    private void simpleBlockUn(Block block) {
        simpleBlock(block, new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath())));
    }

    private void horizontalBlock(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(file)
                        .rotationY(((int) state.getValue(RotatableBlock.FACING_HORIZONTAL).toYRot()) % 360)
                        .build()
                );
    }

    private void logBlockRot(Block block) {
        ModelFile file = new ModelFile.UncheckedModelFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
        getVariantBuilder(block)
                .forAllStates(state -> {
                            Direction.Axis axis = state.getValue(RotatedPillarBlock.AXIS);
                            if (axis == Direction.Axis.Y) {
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .build();
                            }
                            if (axis == Direction.Axis.Z) {
                                return ConfiguredModel.builder()
                                        .modelFile(file)
                                        .rotationX(90)
                                        .build();
                            }
                            return ConfiguredModel.builder()
                                    .modelFile(file)
                                    .rotationX(90)
                                    .rotationY(90)
                                    .build();
                        }
                );
    }

}
