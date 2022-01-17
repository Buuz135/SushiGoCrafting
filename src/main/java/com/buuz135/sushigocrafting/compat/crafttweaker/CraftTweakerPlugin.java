package com.buuz135.sushigocrafting.compat.crafttweaker;

import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraftforge.common.MinecraftForge;

@FeaturePlugin(value = "crafttweaker", type = FeaturePlugin.FeaturePluginType.MOD)
public class CraftTweakerPlugin implements FeaturePluginInstance {
    
    @Override
    public void execute(PluginPhase phase) {
        if(phase == PluginPhase.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }
    /**
    @SubscribeEvent
    public void commands(CTCommandCollectionEvent e) {
        e.registerDump("sushigocrafting_food_ingredients", "Lists all Sushi Go Crafting Food Ingredients", (CommandCaller) context -> {
            
            CraftTweakerAPI.logDump("List of all known Food Ingredients: ");
            FoodAPI.get().getFoodIngredient().forEach(iFoodIngredient -> {
                CraftTweakerAPI.logDump("- %s", iFoodIngredient.getName());
            });
            
            final TextComponent message = new TextComponent(ChatFormatting.GREEN + "Food Ingredients written to the log" + ChatFormatting.RESET);
            context.getSource().sendSuccess(message, true);
            return 0;
        });
    }*/
}
