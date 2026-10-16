package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Checked-in item model assets use vanilla 26.1 item model definitions. */
public final class SushiItemModelProvider implements DataProvider {
    public SushiItemModelProvider(PackOutput output) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Sushi Go Crafting SushiItemModelProvider";
    }
}
