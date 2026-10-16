package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Checked-in 26.1 resources are the source for SushiDataMapProvider. */
public final class SushiDataMapProvider implements DataProvider {
    public SushiDataMapProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Sushi Go Crafting SushiDataMapProvider";
    }
}
