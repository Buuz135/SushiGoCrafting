package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Checked-in blockstate assets replace the removed 26.1 NeoForge builder API. */
public final class SushiBlockstateProvider implements DataProvider {
    public SushiBlockstateProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Sushi Go Crafting SushiBlockstateProvider";
    }
}
