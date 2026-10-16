package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Generates the checked-in 26.1 resource set. */
public final class SushiItemTagsProvider implements DataProvider {
    private final SushiStaticResourceProvider delegate;

    public SushiItemTagsProvider(PackOutput output) {
        this.delegate = new SushiStaticResourceProvider(output, "Sushi Go Crafting SushiItemTagsProvider");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return delegate.run(output);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
