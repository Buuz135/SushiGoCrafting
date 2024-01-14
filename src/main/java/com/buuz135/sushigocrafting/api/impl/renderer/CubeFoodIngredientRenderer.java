package com.buuz135.sushigocrafting.api.impl.renderer;

import com.buuz135.sushigocrafting.api.IFoodIngredientRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class CubeFoodIngredientRenderer implements IFoodIngredientRenderer {

    private final float thickness;
    private final Color color;

    public CubeFoodIngredientRenderer(float height, Color color) {
        this.thickness = height;
        this.color = color;
    }

    @Override
    public Pair<Float, Float> render(PoseStack stack, MultiBufferSource renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn) {
        var paddingX = 0.15f;
        var paddingY = 0.08f;
        var customHeight = thickness + (weight / 2f) * thickness;
        var box = new AABB(paddingX, 0, 0.5, 1 - paddingX, customHeight, 0.5 + customHeight * 2);
        IFoodIngredientRenderer.renderCube(stack, renderTypeBuffer, box, 0, 0, 0, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        return Pair.of(customHeight * 2, 0f);
    }
}
