package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SushiModelProvider extends BlockModelProvider {

    public SushiModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.customCrop(SushiContent.Blocks.RICE_CROP.get(), "rice");
        this.customCrop(SushiContent.Blocks.CUCUMBER_CROP.get(), "cucumber");
        this.customCrop(SushiContent.Blocks.SOY_CROP.get(), "soy");
        this.customCrop(SushiContent.Blocks.WASABI_CROP.get(), "wasabi");
    }

    public void customCrop(CropsBlock block, String name) {
        for (Integer allowedValue : block.getAgeProperty().getAllowedValues()) {
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
