package com.buuz135.sushigocrafting.compat;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.ForgeRegistries;

@FeaturePlugin(value = "patchouli", type = FeaturePlugin.FeaturePluginType.MOD)
public class PatchouliPlugin implements FeaturePluginInstance {


    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            EventManager.mod(BuildCreativeModeTabContentsEvent.class).process(buildCreativeModeTabContentsEvent -> {
                if (SushiGoCrafting.TAB.getResourceLocation().equals(buildCreativeModeTabContentsEvent.getTabKey().location())) {
                    var item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("patchouli", "guide_book")));
                    item.getOrCreateTag().putString("patchouli:book", "sushigocrafting:sushigocrafting");
                    buildCreativeModeTabContentsEvent.accept(item);
                }
            }).subscribe();
        }
    }


}