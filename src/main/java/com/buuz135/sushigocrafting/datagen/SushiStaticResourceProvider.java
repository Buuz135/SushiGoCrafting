package com.buuz135.sushigocrafting.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

final class SushiStaticResourceProvider implements DataProvider {
    private final PackOutput output;
    private final String name;

    SushiStaticResourceProvider(PackOutput output, String name) {
        this.output = output;
        this.name = name;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Path source = Path.of("src", "generated", "resources");
        Path destination = output.getOutputFolder();
        try (var files = Files.walk(source)) {
            files.filter(Files::isRegularFile).forEach(file -> {
                try {
                    Path target = destination.resolve(source.relativize(file));
                    Files.createDirectories(target.getParent());
                    Files.copy(file, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException exception) {
                    throw new IllegalStateException("Unable to copy generated resource " + file, exception);
                }
            });
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to enumerate generated resources", exception);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return name;
    }
}
