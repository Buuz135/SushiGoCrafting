package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.item.SushiItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SushiItemModelProvider implements DataProvider {
    private final PackOutput.PathProvider models;
    private final PackOutput.PathProvider items;

    public SushiItemModelProvider(PackOutput output) {
        models = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        items = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        Set<Item> flatItems = Set.of(SushiContent.Items.WASABI_SEEDS.get(), SushiContent.Items.RICE_SEEDS.get(), SushiContent.Items.SOY_SEEDS.get(),
                SushiContent.Items.SESAME_SEEDS.get(), SushiContent.Items.CUCUMBER_SEEDS.get(), SushiContent.Items.AVOCADO_SAPLING.get());
        for (var holder : SushiContent.Items.REGISTRY.getEntries()) {
            Item item = holder.get();
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            JsonObject model = new JsonObject();
            if (item instanceof BlockItem blockItem && !flatItems.contains(item)) {
                model.addProperty("parent", BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).withPrefix("block/").toString());
            } else {
                model.addProperty("parent", item == SushiContent.Items.SEAWEED_ON_A_STICK.get() ? "minecraft:item/handheld_rod" : "minecraft:item/generated");
                String category = item instanceof SushiItem sushi ? sushi.getCategory() : "";
                JsonObject textures = new JsonObject();
                textures.addProperty("layer0", id.getNamespace() + ":item/" + (category.isEmpty() ? "" : category + "/") + id.getPath());
                model.add("textures", textures);
            }
            writes.add(DataProvider.saveStable(output, model, models.json(id)));
            JsonObject definition = new JsonObject();
            JsonObject itemModel = new JsonObject();
            itemModel.addProperty("type", "minecraft:model");
            itemModel.addProperty("model", id.withPrefix("item/").toString());
            definition.add("model", itemModel);
            writes.add(DataProvider.saveStable(output, definition, items.json(id)));
        }
        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Sushi item models";
    }
}
