package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.CropBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SushiModelProvider implements DataProvider {
    private final PackOutput.PathProvider models;

    public SushiModelProvider(PackOutput output) {
        models = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        crop(writes, output, SushiContent.Blocks.RICE_CROP.get(), "rice", true);
        crop(writes, output, SushiContent.Blocks.CUCUMBER_CROP.get(), "cucumber", false);
        crop(writes, output, SushiContent.Blocks.SOY_CROP.get(), "soy", false);
        crop(writes, output, SushiContent.Blocks.WASABI_CROP.get(), "wasabi", false);
        crop(writes, output, SushiContent.Blocks.SESAME_CROP.get(), "sesame", false);
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private void crop(List<CompletableFuture<?>> writes, CachedOutput output, CropBlock block, String name, boolean skipLastStage) {
        for (int age : CustomCropBlock.AGE.getPossibleValues()) {
            if (skipLastStage && age == 3) continue;
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:block/crop");
            JsonObject textures = new JsonObject();
            textures.addProperty("crop", SushiGoCrafting.MOD_ID + ":block/" + name + "_stage_" + age);
            model.add("textures", textures);
            model.addProperty("render_type", "minecraft:cutout");
            Identifier id = BuiltInRegistries.BLOCK.getKey(block).withSuffix("_" + age);
            writes.add(DataProvider.saveStable(output, model, models.json(id)));
        }
    }

    @Override
    public String getName() {
        return "Sushi block models";
    }
}
