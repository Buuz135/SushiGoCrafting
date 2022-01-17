package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class SushiModelProvider extends BlockModelProvider {

    public SushiModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.customCrop(SushiContent.Blocks.RICE_CROP.get(), "rice", 3);
        this.customCrop(SushiContent.Blocks.CUCUMBER_CROP.get(), "cucumber");
        this.customCrop(SushiContent.Blocks.SOY_CROP.get(), "soy");
        this.customCrop(SushiContent.Blocks.WASABI_CROP.get(), "wasabi");
        this.customCrop(SushiContent.Blocks.SESAME_CROP.get(), "sesame");
    }

    public void customCrop(CropBlock block, String name, Integer... filterValues) {
        for (Integer allowedValue : block.getAgeProperty().getPossibleValues()) {
            if (filterValues != null && Arrays.asList(filterValues).contains(allowedValue)) continue;
            getBuilder(block.getRegistryName().getPath() + "_" + allowedValue).parent(getUnchecked(mcLoc(BLOCK_FOLDER + "/crop"))).texture("crop", modLoc(BLOCK_FOLDER + "/" + name + "_stage_" + allowedValue));
        }

    }

    public ModelFile.UncheckedModelFile getUnchecked(ResourceLocation path) {
        ModelFile.UncheckedModelFile ret = new ModelFile.UncheckedModelFile(extendWithFolder(path));
        ret.assertExistence();
        return ret;
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
    }

}
