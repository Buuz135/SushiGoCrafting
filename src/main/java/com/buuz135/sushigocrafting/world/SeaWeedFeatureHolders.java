package com.buuz135.sushigocrafting.world;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SeaWeedFeatureHolders {
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> HOLDER = FeatureUtils.register("seaweed", SushiContent.Features.SEAWEED.get());
    public static Holder<PlacedFeature> PLACEMENT = PlacementUtils.register("seaweed", HOLDER, NoiseBasedCountPlacement.of(80, 80.0D, 0.0D), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome());
}
