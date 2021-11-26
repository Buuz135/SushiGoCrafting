package com.buuz135.sushigocrafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.impl.commands.CTCommandCollectionEvent;
import com.blamejared.crafttweaker.impl.commands.CommandCaller;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@FeaturePlugin(value = "crafttweaker", type = FeaturePlugin.FeaturePluginType.MOD)
public class CraftTweakerPlugin implements FeaturePluginInstance {
    
    @Override
    public void execute(PluginPhase phase) {
        if(phase == PluginPhase.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }
    
    @SubscribeEvent
    public void commands(CTCommandCollectionEvent e) {
        e.registerDump("sushigocrafting_food_ingredients", "Lists all Sushi Go Crafting Food Ingredients", (CommandCaller) context -> {
            
            CraftTweakerAPI.logDump("List of all known Food Ingredients: ");
            FoodAPI.get().getFoodIngredient().forEach(iFoodIngredient -> {
                CraftTweakerAPI.logDump("- %s", iFoodIngredient.getName());
            });
            
            final StringTextComponent message = new StringTextComponent(TextFormatting.GREEN + "Food Ingredients written to the log" + TextFormatting.RESET);
            context.getSource().sendFeedback(message, true);
            return 0;
        });
    }
}
