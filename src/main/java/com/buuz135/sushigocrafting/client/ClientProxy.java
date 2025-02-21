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
import com.buuz135.sushigocrafting.client.render.ContributorsBackRender;
import com.buuz135.sushigocrafting.client.tesr.CuttingBoardRenderer;
import com.buuz135.sushigocrafting.client.tesr.RollerRenderer;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientProxy {

    public static BakedModel SALMON_BACK;
    public static BakedModel TUNA_BACK;

    public static void register() {
        EventManager.mod(EntityRenderersEvent.RegisterRenderers.class).process(event -> {
            event.registerEntityRenderer(SushiContent.EntityTypes.TUNA.get(), TunaRenderer::new);
            event.registerEntityRenderer(SushiContent.EntityTypes.SHRIMP.get(), ShrimpRenderer::new);
            event.registerBlockEntityRenderer(SushiContent.TileEntities.CUTTING_BOARD.get(), p_173571_ -> new CuttingBoardRenderer());
            event.registerBlockEntityRenderer(SushiContent.TileEntities.ROLLER.get(), p_173571_ -> new RollerRenderer());

        }).subscribe();
        EventManager.mod(EntityRenderersEvent.AddLayers.class).process(event -> {
            for (PlayerSkin.Model skin : event.getSkins()) {
                var renderer = event.getSkin(skin);
                if (renderer instanceof PlayerRenderer playerRenderer) {
                    playerRenderer.addLayer(new ContributorsBackRender(playerRenderer));
                }
            }
        }).subscribe();
        EventManager.mod(EntityRenderersEvent.RegisterLayerDefinitions.class).process(event -> {
            event.registerLayerDefinition(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "shrimp"), "main"), ShrimpModel::createBodyLayer);
        }).subscribe();
        EventManager.mod(ModelEvent.RegisterAdditional.class).process(event -> {
            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "block/salmon_back")));
            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "block/tuna_back")));
        }).subscribe();
        EventManager.mod(ModelEvent.BakingCompleted.class).process(event -> {
            SALMON_BACK = event.getModels().get(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "block/salmon_back")));
            TUNA_BACK = event.getModels().get(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "block/tuna_back")));
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
        //ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SEAWEED.get(), RenderType.cutout());
        //ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SEAWEED_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(SushiContent.Blocks.SESAME_CROP.get(), RenderType.cutout());
        EventManager.forge(ItemTooltipEvent.class).process(event -> {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(event.getItemStack().getItem());
            if (!ingredient.isEmpty() && ingredient.getEffect() != null) {
                event.getToolTip().add(Component.literal(""));
                if (Screen.hasShiftDown()) {
                    IIngredientEffect effect = ingredient.getEffect();
                    if (effect instanceof AddIngredientEffect) {
                        event.getToolTip().add(Component.literal("" + ChatFormatting.DARK_AQUA + Component.translatable("text.sushigocrafting.add_food_effect").getString() + ":"));
                        event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + ((AddIngredientEffect) effect).getEffect().value().getDisplayName().getString() + ChatFormatting.DARK_AQUA + " (" + ChatFormatting.WHITE + ((AddIngredientEffect) effect).getDuration() / 20 + ChatFormatting.YELLOW + "s" + ChatFormatting.DARK_AQUA + ", " + ChatFormatting.YELLOW + "Level " + ChatFormatting.WHITE + (((AddIngredientEffect) effect).getLevel() + 1) + ChatFormatting.DARK_AQUA + ")"));
                    }
                    if (effect instanceof ModifyIngredientEffect) {
                        event.getToolTip().add(Component.literal("" + ChatFormatting.DARK_AQUA + Component.translatable("text.sushigocrafting.modify_food_effect").getString()+ ":"));
                        if (((ModifyIngredientEffect) effect).getTimeModifier() != 1) {
                            event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + " " + Component.translatable("text.sushigocrafting.multiply_time_by").getString() + " " + ChatFormatting.WHITE + ((ModifyIngredientEffect) effect).getTimeModifier()));
                        }
                        if (((ModifyIngredientEffect) effect).getLevelModifier() > 0)
                            event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + " " + Component.translatable("text.sushigocrafting.increase_level_by").getString() + " " + ChatFormatting.WHITE + ((ModifyIngredientEffect) effect).getLevelModifier()));
                    }
                } else {
                    event.getToolTip().add(Component.literal(ChatFormatting.YELLOW + "" + Component.translatable("text.sushigocrafting.hold").getString() + ChatFormatting.GOLD + " " + ChatFormatting.ITALIC + "<" + Component.translatable("key.keyboard.left.shift").getString() + ">" + ChatFormatting.RESET + ChatFormatting.YELLOW + Component.translatable("text.sushigocrafting.sushi_effect").getString()));
                }
            }
        }).subscribe();
    }
}
