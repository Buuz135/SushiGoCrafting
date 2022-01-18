package com.buuz135.sushigocrafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.command.CommandUtilities;
import com.blamejared.crafttweaker.api.command.boilerplate.CommandImpl;
import com.blamejared.crafttweaker.api.event.type.CTCommandRegisterEvent;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.plugin.FeaturePluginInstance;
import com.hrznstudio.titanium.plugin.PluginPhase;
import com.mojang.brigadier.Command;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@FeaturePlugin(value = "crafttweaker", type = FeaturePlugin.FeaturePluginType.MOD)
public class CraftTweakerPlugin implements FeaturePluginInstance {

    @Override
    public void execute(PluginPhase phase) {
        if (phase == PluginPhase.COMMON_SETUP) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void commands(CTCommandRegisterEvent e) {
        e.registerCommand(new CommandImpl("sushigocrafting_food_ingredients", new TextComponent("Lists all Sushi Go Crafting Food Ingredients"), builder -> {
            builder.executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CraftTweakerAPI.LOGGER.info("List of all known Food Ingredients: ");
                FoodAPI.get().getFoodIngredient().forEach(iFoodIngredient -> {
                    CraftTweakerAPI.LOGGER.info("- {}", iFoodIngredient.getName());
                });

                CommandUtilities.send(CommandUtilities.openingLogFile(new TranslatableComponent("crafttweaker.command.list.check.log", CommandUtilities.makeNoticeable(new TextComponent("Food Ingredients")), CommandUtilities.getFormattedLogFile()).withStyle(ChatFormatting.GREEN)), player);
                return Command.SINGLE_SUCCESS;
            });
        }));
    }
}
