package com.buuz135.sushigocrafting.client.tesr;

import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;

public class RollerRenderer implements BlockEntityRenderer<RollerTile> {

    public RollerRenderer() {

    }

    @Override
    public void render(RollerTile tileEntityIn, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrix.pushPose();
        Direction facing = tileEntityIn.getFacingDirection();
        var height = 0.03;
        switch (facing) {
            case SOUTH:
                matrix.translate(1, height, 1);
                matrix.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case NORTH:
                matrix.translate(0, height, 0);
                break;
            case EAST:
                matrix.translate(1, height, 0);
                matrix.mulPose(Axis.YN.rotationDegrees(90));
                break;
            case WEST:
                matrix.translate(0, height, 1);
                matrix.mulPose(Axis.YP.rotationDegrees(90));
                break;
        }
        for (int i = 0; i < tileEntityIn.getSpices().getSlots(); i++) {
            var stack = tileEntityIn.getSpices().getStackInSlot(i);
            if (!stack.isEmpty()) {
                var ingredient = FoodAPI.get().getIngredientFromItem(stack.getItem());
                if (!ingredient.isEmpty()) {
                    var renderer = ingredient.getRenderer();
                    if (renderer != null) {
                        var offset = renderer.render(matrix, bufferIn, 0, combinedLightIn, combinedOverlayIn);
                        matrix.translate(0, offset.getSecond(), offset.getFirst());
                    }
                }
            }
        }
        for (int i = 0; i < tileEntityIn.getSlots().getSlots(); i++) {
            var stack = tileEntityIn.getSlots().getStackInSlot(i);
            if (!stack.isEmpty()) {
                var ingredient = FoodAPI.get().getIngredientFromItem(stack.getItem());
                if (!ingredient.isEmpty()) {
                    var renderer = ingredient.getRenderer();
                    if (renderer != null) {
                        var offset = renderer.render(matrix, bufferIn, tileEntityIn.getWeightTracker().getWeights().get(i), combinedLightIn, combinedOverlayIn);
                        matrix.translate(0, offset.getSecond(), offset.getFirst());
                    }
                }
            }
        }

        matrix.popPose();
    }
}
