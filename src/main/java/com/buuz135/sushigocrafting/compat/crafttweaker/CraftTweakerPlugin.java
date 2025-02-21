package com.buuz135.sushigocrafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.impl.command.CtCommands;
import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import com.mojang.brigadier.Command;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@FeaturePlugin(value = "crafttweaker", type = FeaturePlugin.FeaturePluginType.MOD)
public class CraftTweakerPlugin implements FeaturePluginInstance {

    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.COMMON_SETUP) {
            CtCommands.get().registerCommand("sushigocrafting_food_ingredients", Component.translatable("text.sushigocrafting.list_ingredients"), builder -> {
                builder.executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CraftTweakerAPI.getLogger(SushiGoCrafting.MOD_ID).info(Component.translatable("text.sushigocrafting.list_of_ingredients") + ":");
                    FoodAPI.get().getFoodIngredient().forEach(iFoodIngredient -> {
                        CraftTweakerAPI.getLogger(SushiGoCrafting.MOD_ID).info("- {}", iFoodIngredient.getName());
                    });

                    //TODO CommandUtilities.send(CommandUtilities.openingUrl(Component.translatable("crafttweaker.command.list.check.log", CommandUtilities.makeNoticeable(Component.literal("Food Ingredients")), CommandUtilities.getFormattedLogFile()).withStyle(ChatFormatting.GREEN)), player);
                    return Command.SINGLE_SUCCESS;
                });
            });

        }
    }
}
