package com.buuz135.sushigocrafting.client;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.effect.AddIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.client.entity.ShrimpRenderer;
import com.buuz135.sushigocrafting.client.entity.TunaRenderer;
import com.buuz135.sushigocrafting.client.entity.model.ShrimpModel;
import com.buuz135.sushigocrafting.client.tesr.CuttingBoardRenderer;
import com.buuz135.sushigocrafting.client.tesr.RollerRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientProxy {

    public static void register() {
        EventManager.mod(EntityRenderersEvent.RegisterRenderers.class).process(event -> {
            event.registerEntityRenderer(SushiContent.EntityTypes.TUNA.get(), TunaRenderer::new);
            event.registerEntityRenderer(SushiContent.EntityTypes.SHRIMP.get(), ShrimpRenderer::new);
            event.registerBlockEntityRenderer(SushiContent.TileEntities.CUTTING_BOARD.get(), p_173571_ -> new CuttingBoardRenderer());
            event.registerBlockEntityRenderer(SushiContent.TileEntities.ROLLER.get(), p_173571_ -> new RollerRenderer());

        }).subscribe();
        EventManager.mod(EntityRenderersEvent.RegisterLayerDefinitions.class).process(event -> {
            event.registerLayerDefinition(new ModelLayerLocation(Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "shrimp"), "main"), ShrimpModel::createBodyLayer);
        }).subscribe();
    }

    public void fmlClient(FMLClientSetupEvent fml) {
        EventManager.forge(ItemTooltipEvent.class).process(event -> {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(event.getItemStack().getItem());
            if (!ingredient.isEmpty() && ingredient.getEffect() != null) {
                event.getToolTip().add(Component.literal(""));
                if (Screen.hasShiftDown()) {
                    IIngredientEffect effect = ingredient.getEffect();
                    if (effect instanceof AddIngredientEffect) {
                        event.getToolTip().add(Component.literal(ChatFormatting.DARK_AQUA + "Adds Food Effect:"));
                        event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + ((AddIngredientEffect) effect).getEffect().value().getDisplayName().getString() + ChatFormatting.DARK_AQUA + " (" + ChatFormatting.WHITE + ((AddIngredientEffect) effect).getDuration() / 20 + ChatFormatting.YELLOW + "s" + ChatFormatting.DARK_AQUA + ", " + ChatFormatting.YELLOW + "Level " + ChatFormatting.WHITE + (((AddIngredientEffect) effect).getLevel() + 1) + ChatFormatting.DARK_AQUA + ")"));
                    }
                    if (effect instanceof ModifyIngredientEffect) {
                        event.getToolTip().add(Component.literal(ChatFormatting.DARK_AQUA + "Modifies Food Effect:"));
                        if (((ModifyIngredientEffect) effect).getTimeModifier() != 1) {
                            event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + " Multiplies Time By " + ChatFormatting.WHITE + ((ModifyIngredientEffect) effect).getTimeModifier()));
                        }
                        if (((ModifyIngredientEffect) effect).getLevelModifier() > 0)
                            event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + " Increases Level By " + ChatFormatting.WHITE + ((ModifyIngredientEffect) effect).getLevelModifier()));
                    }
                } else {
                    event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + "Hold " + ChatFormatting.GOLD + ChatFormatting.ITALIC + "<Shift>" + ChatFormatting.RESET + ChatFormatting.YELLOW + " for sushi effect"));
                }
            }
        }).subscribe();
    }
}
