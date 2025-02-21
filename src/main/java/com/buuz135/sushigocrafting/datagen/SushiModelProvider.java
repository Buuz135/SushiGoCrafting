package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class SushiModelProvider extends BlockModelProvider {

    public SushiModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modid, existingFileHelper);
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
        for (Integer allowedValue : CustomCropBlock.AGE.getPossibleValues()) {
            if (filterValues != null && Arrays.asList(filterValues).contains(allowedValue)) continue;
            getBuilder(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_" + allowedValue).parent(getUnchecked(mcLoc(BLOCK_FOLDER + "/crop"))).texture("crop", modLoc(BLOCK_FOLDER + "/" + name + "_stage_" + allowedValue));
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
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), folder + "/" + rl.getPath());
    }

}
