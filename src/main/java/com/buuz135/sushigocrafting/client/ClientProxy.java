package com.buuz135.sushigocrafting.client;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.effect.AddIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.client.entity.ItamaeCatRenderer;
import com.buuz135.sushigocrafting.client.entity.ShrimpRenderer;
import com.buuz135.sushigocrafting.client.entity.TunaRenderer;
import com.buuz135.sushigocrafting.client.entity.model.ItamaeCatModel;
import com.buuz135.sushigocrafting.client.entity.model.ShrimpModel;
import com.buuz135.sushigocrafting.client.render.ContributorsBackRender;
import com.buuz135.sushigocrafting.client.tesr.CuttingBoardRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy {

    public static BakedModel SALMON_BACK;
    public static BakedModel TUNA_BACK;

    public static void register() {
        EventManager.mod(EntityRenderersEvent.RegisterRenderers.class).process(event -> {
            event.registerEntityRenderer(SushiContent.EntityTypes.TUNA.get(), TunaRenderer::new);
            event.registerEntityRenderer(SushiContent.EntityTypes.SHRIMP.get(), ShrimpRenderer::new);
            event.registerBlockEntityRenderer(SushiContent.TileEntities.CUTTING_BOARD.get(), p_173571_ -> new CuttingBoardRenderer());
            event.registerEntityRenderer(SushiContent.EntityTypes.ITAMAE_CAT.get(), ItamaeCatRenderer::new);
        }).subscribe();
        EventManager.mod(EntityRenderersEvent.AddLayers.class).process(event -> {
            for (String skin : event.getSkins()) {
                PlayerRenderer renderer = event.getSkin(skin);
                renderer.addLayer(new ContributorsBackRender(renderer));
            }
        }).subscribe();
        EventManager.mod(EntityRenderersEvent.RegisterLayerDefinitions.class).process(event -> {
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(SushiGoCrafting.MOD_ID, "shrimp"), "main"), ShrimpModel::createBodyLayer);
            event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(SushiGoCrafting.MOD_ID, "itamae_cat"), "main"), ItamaeCatModel::createBodyLayer);
        }).subscribe();
        EventManager.mod(ModelEvent.RegisterAdditional.class).process(event -> {
            event.register(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/salmon_back"));
            event.register(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/tuna_back"));
        }).subscribe();
        EventManager.mod(ModelEvent.BakingCompleted.class).process(event -> {
            SALMON_BACK = event.getModels().get(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/salmon_back"));
            TUNA_BACK = event.getModels().get(new ResourceLocation(SushiGoCrafting.MOD_ID, "block/tuna_back"));
        }).subscribe();
    }

    public void fmlClient(FMLClientSetupEvent fml) {
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.RICE_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.CUCUMBER_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SOY_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.WASABI_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES_LOG.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.AVOCADO_LEAVES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.AVOCADO_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SEAWEED.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SEAWEED_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SESAME_CROP.get(), RenderType.cutout());
        EventManager.forge(ItemTooltipEvent.class).process(event -> {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(event.getItemStack().getItem());
            if (!ingredient.isEmpty() && ingredient.getEffect() != null) {
                event.getToolTip().add(Component.literal(""));
                if (Screen.hasShiftDown()) {
                    IIngredientEffect effect = ingredient.getEffect();
                    if (effect instanceof AddIngredientEffect) {
                        event.getToolTip().add(Component.literal(ChatFormatting.DARK_AQUA + "Adds Food Effect:"));
                        event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + ((AddIngredientEffect) effect).getEffect().get().getDisplayName().getString() + ChatFormatting.DARK_AQUA + " (" + ChatFormatting.WHITE + ((AddIngredientEffect) effect).getDuration() / 20 + ChatFormatting.YELLOW + "s" + ChatFormatting.DARK_AQUA + ", " + ChatFormatting.YELLOW + "Level " + ChatFormatting.WHITE + (((AddIngredientEffect) effect).getLevel() + 1) + ChatFormatting.DARK_AQUA + ")"));
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
                    event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + "Hold " + ChatFormatting.GOLD + "" + ChatFormatting.ITALIC + "<Shift>" + ChatFormatting.RESET + ChatFormatting.YELLOW + " for sushi effect"));
                }
            }
        }).subscribe();
    }
}
