package com.buuz135.sushigocrafting.compat;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.item.PatchouliDataComponents;

@FeaturePlugin(value = "patchouli", type = FeaturePlugin.FeaturePluginType.MOD)
public class PatchouliPlugin implements FeaturePluginInstance {


    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            EventManager.mod(BuildCreativeModeTabContentsEvent.class).process(buildCreativeModeTabContentsEvent -> {
                if (SushiGoCrafting.TAB.getResourceLocation().equals(buildCreativeModeTabContentsEvent.getTabKey().location())) {
                    var item = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book")));
                    item.set(PatchouliDataComponents.BOOK, ResourceLocation.fromNamespaceAndPath("sushigocrafting", "sushigocrafting"));
                    buildCreativeModeTabContentsEvent.accept(item);
                }
            }).subscribe();
        }
    }


}