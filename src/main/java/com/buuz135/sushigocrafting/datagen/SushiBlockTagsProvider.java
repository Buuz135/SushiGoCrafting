package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Checked-in 26.1 resources are the source for SushiBlockTagsProvider. */
public final class SushiBlockTagsProvider implements DataProvider {
    public SushiBlockTagsProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Sushi Go Crafting SushiBlockTagsProvider";
    }
}
