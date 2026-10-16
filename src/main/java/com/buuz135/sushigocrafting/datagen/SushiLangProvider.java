package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

/** Generates the checked-in 26.1 resource set. */
public final class SushiLangProvider implements DataProvider {
    private final SushiStaticResourceProvider delegate;

    public SushiLangProvider(PackOutput output) {
        this.delegate = new SushiStaticResourceProvider(output, "Sushi Go Crafting SushiLangProvider");
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
