package com.buuz135.sushigocrafting.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class AvocadoTreeGrower extends AbstractTreeGrower {

    private ResourceKey<ConfiguredFeature<?, ?>> holder;

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean p_222911_) {
        if (holder == null)
            holder = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("sushigocrafting", "avocado_tree"));
        return holder;
    }
}
