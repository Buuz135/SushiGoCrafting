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

    static RenderType ROLLER_RENDERER = createRenderType();

    public static RenderType createRenderType() {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/block/roller_texture.png"), false, false))
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexShader))
                .createCompositeState(true);
        return RenderType.create("roller_renderer", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true, state);
    }

    public static void renderCube(PoseStack stack, MultiBufferSource renderTypeBuffer, AABB pos, double x, double y, double z, float red, float green, float blue, float alpha) {

        float x1 = (float) (pos.minX + x);
        float x2 = (float) (pos.maxX + x);
        float y1 = (float) (pos.minY + y);
        float y2 = (float) (pos.maxY + y);
        float z1 = (float) (pos.minZ + z);
        float z2 = (float) (pos.maxZ + z);

        Matrix4f matrix = stack.last().pose();
        VertexConsumer buffer;

        buffer = renderTypeBuffer.getBuffer(ROLLER_RENDERER);

        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(0, (float) pos.maxY).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).uv(0, 0).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).uv(1, 0).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).uv(1, (float) pos.maxY).endVertex();

        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).uv(0, 1).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).uv(0, 0).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv((float) pos.maxY, 0).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).uv((float) pos.maxY, 1).endVertex();


        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(0, 1).endVertex();
        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).uv(0, 0).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).uv(1, 0).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).uv(0, 0).endVertex();

        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).uv(0, (float) pos.maxZ).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).uv(0, (float) pos.minZ).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv(1, (float) pos.minZ).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).uv(1, (float) pos.maxZ).endVertex();


        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(0, (float) pos.maxZ).endVertex();
        buffer.vertex(matrix, x1, y1, z2).color(red, green, blue, alpha).uv(0, (float) pos.minZ).endVertex();
        buffer.vertex(matrix, x1, y2, z2).color(red, green, blue, alpha).uv((float) pos.maxY, (float) pos.minZ).endVertex();
        buffer.vertex(matrix, x1, y2, z1).color(red, green, blue, alpha).uv((float) pos.maxY, (float) pos.maxZ).endVertex();

        buffer.vertex(matrix, x2, y1, z1).color(red, green, blue, alpha).uv((float) pos.maxY, (float) pos.maxZ).endVertex();
        buffer.vertex(matrix, x2, y2, z1).color(red, green, blue, alpha).uv(0, (float) pos.maxZ).endVertex();
        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv(0, (float) pos.minZ).endVertex();
        buffer.vertex(matrix, x2, y1, z2).color(red, green, blue, alpha).uv((float) pos.maxY, (float) pos.minZ).endVertex();

    }

    Pair<Float, Float> render(PoseStack stack, MultiBufferSource renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn);
}
