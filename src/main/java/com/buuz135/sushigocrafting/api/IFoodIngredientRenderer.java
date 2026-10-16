package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;

public interface IFoodIngredientRenderer {

    RenderType ROLLER_RENDERER = createRenderType();

    static RenderType createRenderType() {
        return RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "textures/block/roller_texture.png"));
    }

    static void renderCube(PoseStack stack, SubmitNodeCollector renderTypeBuffer, AABB pos, double x, double y, double z, float red, float green, float blue, float alpha) {

        float x1 = (float) (pos.minX + x);
        float x2 = (float) (pos.maxX + x);
        float y1 = (float) (pos.minY + y);
        float y2 = (float) (pos.maxY + y);
        float z1 = (float) (pos.minZ + z);
        float z2 = (float) (pos.maxZ + z);

        renderTypeBuffer.submitCustomGeometry(stack, ROLLER_RENDERER, (pose, buffer) -> {
        var matrix = pose.pose();
        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxY).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv(1, (float) pos.maxY).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);

        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);


        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, 1).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv(1, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, 0).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);

        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(1, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(1, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);


        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);

        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.minZ).setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).setLight(net.minecraft.util.LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
        });

    }

    Pair<Float, Float> render(PoseStack stack, SubmitNodeCollector renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn);
}
