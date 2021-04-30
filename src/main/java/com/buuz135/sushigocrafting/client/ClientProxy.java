package com.buuz135.sushigocrafting.client;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.effect.AddIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.client.entity.ShrimpRenderer;
import com.buuz135.sushigocrafting.client.entity.TunaRenderer;
import com.buuz135.sushigocrafting.client.tesr.CuttingBoardRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy {

    public void fmlClient(FMLClientSetupEvent fml) {
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.RICE_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.CUCUMBER_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SOY_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.WASABI_CROP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.AVOCADO_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SEAWEED.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SEAWEED_PLANT.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(SushiContent.Blocks.SESAME_CROP.get(), RenderType.getCutout());
        RenderingRegistry.registerEntityRenderingHandler(SushiContent.EntityTypes.TUNA.get(), TunaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SushiContent.EntityTypes.SHRIMP.get(), ShrimpRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SushiContent.TileEntities.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
        EventManager.forge(ItemTooltipEvent.class).process(event -> {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(event.getItemStack().getItem());
            if (!ingredient.isEmpty() && ingredient.getEffect() != null) {
                event.getToolTip().add(new StringTextComponent(""));
                if (Screen.hasShiftDown()) {
                    IIngredientEffect effect = ingredient.getEffect();
                    if (effect instanceof AddIngredientEffect) {
                        event.getToolTip().add(new StringTextComponent(TextFormatting.DARK_AQUA + "Adds Food Effect:"));
                        event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + ((AddIngredientEffect) effect).getEffect().get().getDisplayName().getString() + TextFormatting.DARK_AQUA + " (" + TextFormatting.WHITE + ((AddIngredientEffect) effect).getDuration() / 20 + TextFormatting.YELLOW + "s" + TextFormatting.DARK_AQUA + ", " + TextFormatting.YELLOW + "Level " + TextFormatting.WHITE + (((AddIngredientEffect) effect).getLevel() + 1) + TextFormatting.DARK_AQUA + ")"));
                    }
                    if (effect instanceof ModifyIngredientEffect) {
                        event.getToolTip().add(new StringTextComponent(TextFormatting.DARK_AQUA + "Modifies Food Effect:"));
                        if (((ModifyIngredientEffect) effect).getTimeModifier() != 1) {
                            event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + " Multiplies Time By " + TextFormatting.WHITE + ((ModifyIngredientEffect) effect).getTimeModifier()));
                        }
                        if (((ModifyIngredientEffect) effect).getLevelModifier() > 0)
                            event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + " Increases Level By " + TextFormatting.WHITE + ((ModifyIngredientEffect) effect).getLevelModifier()));
                    }
                } else {
                    event.getToolTip().add(new StringTextComponent(TextFormatting.YELLOW + "Hold " + TextFormatting.GOLD + "" + TextFormatting.ITALIC + "<Shift>" + TextFormatting.RESET + TextFormatting.YELLOW + " for sushi effect"));
                }
            }
        }).subscribe();
    }
}
