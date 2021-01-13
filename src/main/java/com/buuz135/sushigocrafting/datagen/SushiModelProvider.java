package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
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

        this.customCrop("rice_crop", SushiContent.Blocks.RICE_CROP.get().getRegistryName());
    }

    public void customCrop(String id, ResourceLocation resourceLocation) {
        getBuilder("rice_crop").parent(getUnchecked(mcLoc(BLOCK_FOLDER + "/crop"))).texture(id, resourceLocation);
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
