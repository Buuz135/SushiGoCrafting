package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public interface IFoodIngredientRenderer {

    RenderType ROLLER_RENDERER = createRenderType();

    static RenderType createRenderType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "textures/block/roller_texture.png"), false, false))
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionTexColorShader))
                .createCompositeState(true);
        return RenderType.create("roller_renderer", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, false, true, state);
    }

    static void renderCube(PoseStack stack, MultiBufferSource renderTypeBuffer, AABB pos, double x, double y, double z, float red, float green, float blue, float alpha) {

        float x1 = (float) (pos.minX + x);
        float x2 = (float) (pos.maxX + x);
        float y1 = (float) (pos.minY + y);
        float y2 = (float) (pos.maxY + y);
        float z1 = (float) (pos.minZ + z);
        float z2 = (float) (pos.maxZ + z);

        Matrix4f matrix = stack.last().pose();
        VertexConsumer buffer;

        buffer = renderTypeBuffer.getBuffer(ROLLER_RENDERER);

        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxY);
        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv(0, 0);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(1, 0);
        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv(1, (float) pos.maxY);

        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, 1);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv(0, 0);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, 0);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, 1);


        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, 1);
        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv(0, 0);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv(1, 0);
        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, 0);

        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(1, (float) pos.minZ);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(1, (float) pos.maxZ);


        buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ);
        buffer.addVertex(matrix, x1, y1, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ);
        buffer.addVertex(matrix, x1, y2, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.minZ);
        buffer.addVertex(matrix, x1, y2, z1).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.maxZ);

        buffer.addVertex(matrix, x2, y1, z1).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.maxZ);
        buffer.addVertex(matrix, x2, y2, z1).setColor(red, green, blue, alpha).setUv(0, (float) pos.maxZ);
        buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(0, (float) pos.minZ);
        buffer.addVertex(matrix, x2, y1, z2).setColor(red, green, blue, alpha).setUv((float) pos.maxY, (float) pos.minZ);

    }

    Pair<Float, Float> render(PoseStack stack, MultiBufferSource renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn);
}
