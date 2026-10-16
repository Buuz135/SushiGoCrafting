package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.block.plant.CustomCropBlock;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SushiBlockstateProvider implements DataProvider {
    private final PackOutput.PathProvider states;

    public SushiBlockstateProvider(PackOutput output) {
        states = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        for (CustomCropBlock crop : new CustomCropBlock[]{SushiContent.Blocks.RICE_CROP.get(), SushiContent.Blocks.CUCUMBER_CROP.get(), SushiContent.Blocks.WASABI_CROP.get(), SushiContent.Blocks.SOY_CROP.get(), SushiContent.Blocks.SESAME_CROP.get()}) {
            JsonObject variants = new JsonObject();
            for (int age : CustomCropBlock.AGE.getPossibleValues()) variants.add("age=" + age, model(crop, "_" + age, 0, 0));
            save(writes, output, crop, variants);
        }
        for (Block block : new Block[]{SushiContent.Blocks.AVOCADO_SAPLING.get(), SushiContent.Blocks.FERMENTATION_BARREL.get()}) {
            JsonObject variants = new JsonObject();
            variants.add("", model(block, "", 0, 0));
            save(writes, output, block, variants);
        }
        for (Block block : new Block[]{SushiContent.Blocks.RICE_COOKER.get(), SushiContent.Blocks.CUTTING_BOARD.get(), SushiContent.Blocks.ROLLER.get()}) {
            JsonObject variants = new JsonObject();
            variants.add("facing=north", model(block, "", 0, 180));
            variants.add("facing=east", model(block, "", 0, 270));
            variants.add("facing=south", model(block, "", 0, 0));
            variants.add("facing=west", model(block, "", 0, 90));
            save(writes, output, block, variants);
        }
        for (Block block : new Block[]{SushiContent.Blocks.AVOCADO_LOG.get(), SushiContent.Blocks.AVOCADO_LEAVES_LOG.get()}) {
            JsonObject variants = new JsonObject();
            variants.add("axis=y", model(block, "", 0, 0));
            variants.add("axis=z", model(block, "", 90, 0));
            variants.add("axis=x", model(block, "", 90, 90));
            save(writes, output, block, variants);
        }
        JsonObject leaves = new JsonObject();
        for (int stage = 0; stage < 3; stage++) leaves.add("stage=" + stage, model(SushiContent.Blocks.AVOCADO_LEAVES.get(), "_" + stage, 0, 0));
        save(writes, output, SushiContent.Blocks.AVOCADO_LEAVES.get(), leaves);
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private JsonObject model(Block block, String suffix, int x, int y) {
        JsonObject model = new JsonObject();
        model.addProperty("model", BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix(suffix).toString());
        if (x != 0) model.addProperty("x", x);
        if (y != 0) model.addProperty("y", y);
        return model;
    }

    private void save(List<CompletableFuture<?>> writes, CachedOutput output, Block block, JsonObject variants) {
        JsonObject state = new JsonObject();
        state.add("variants", variants);
        writes.add(DataProvider.saveStable(output, state, states.json(BuiltInRegistries.BLOCK.getKey(block))));
    }

    @Override
    public String getName() {
        return "Sushi blockstates";
    }
}
