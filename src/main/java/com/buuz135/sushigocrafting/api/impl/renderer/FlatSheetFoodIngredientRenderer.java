package com.buuz135.sushigocrafting.api.impl.renderer;

import com.buuz135.sushigocrafting.api.IFoodIngredientRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class FlatSheetFoodIngredientRenderer implements IFoodIngredientRenderer {

    private final float height;
    private final Color color;

    public FlatSheetFoodIngredientRenderer(float height, Color color) {
        this.height = height;
        this.color = color;
    }

    @Override
    public Pair<Float, Float> render(PoseStack stack, MultiBufferSource renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn) {
        var paddingX = 0.15f;
        var paddingY = 0.08f;
        var customHeight = height + (weight / 2f) * height;
        var box = new AABB(paddingX, 0, paddingY, 1 - paddingX, customHeight, 1 - paddingY);
        IFoodIngredientRenderer.renderCube(stack, renderTypeBuffer, box, 0, 0, 0, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        return Pair.of(0f, customHeight);
    }
}
