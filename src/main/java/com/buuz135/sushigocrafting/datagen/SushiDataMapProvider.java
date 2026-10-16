package com.buuz135.sushigocrafting.datagen;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class SushiDataMapProvider extends DataMapProvider {

    public SushiDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(SushiContent.Items.RICE_SEEDS.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.CUCUMBER_SEEDS.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.SOY_SEEDS.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.WASABI_SEEDS.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.SESAME_SEEDS.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.AVOCADO_LEAVES.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.AVOCADO_SAPLING.getId(), new Compostable(0.3f), false)
                .add(SushiContent.Items.AVOCADO.getId(), new Compostable(0.65f), false)
                .add(SushiContent.Items.CUCUMBER.getId(), new Compostable(0.65f), false)
                .add(SushiContent.Items.SOY_BEAN.getId(), new Compostable(0.65f), false)
                .add(SushiContent.Items.WASABI_ROOT.getId(), new Compostable(0.65f), false);
    }
}
