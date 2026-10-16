package com.buuz135.sushigocrafting.compat;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@FeaturePlugin(value = "patchouli", type = FeaturePlugin.FeaturePluginType.MOD)
public class PatchouliPlugin implements FeaturePluginInstance {


    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.CONSTRUCTION) {
            EventManager.mod(BuildCreativeModeTabContentsEvent.class).process(buildCreativeModeTabContentsEvent -> {
                if (SushiGoCrafting.TAB.getIdentifier().equals(buildCreativeModeTabContentsEvent.getTabKey().identifier())) {
                    var item = PatchouliAPI.get().getBookStack(Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "sushigocrafting"));
                    buildCreativeModeTabContentsEvent.accept(item);
                }
            }).subscribe();
        }
    }


}
