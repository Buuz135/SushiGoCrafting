package com.buuz135.sushigocrafting.client.tesr;

import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;

public class RollerRenderer implements BlockEntityRenderer<RollerTile, RollerRenderer.State> {

    public static final class State extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState {
        private Direction facing;
        private final java.util.List<Entry> entries = new java.util.ArrayList<>();
    }
    private record Entry(com.buuz135.sushigocrafting.api.IFoodIngredientRenderer renderer, int weight) { }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(RollerTile tileEntityIn, State state, float partialTicks, net.minecraft.world.phys.Vec3 camera, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState.extractBase(tileEntityIn, state, breakProgress);
        state.facing = tileEntityIn.getFacingDirection();
        state.entries.clear();
        for (int i = 0; i < tileEntityIn.getSpices().getSlots(); i++) {
            var stack = tileEntityIn.getSpices().getStackInSlot(i);
            if (!stack.isEmpty()) {
                var ingredient = com.buuz135.sushigocrafting.api.impl.FoodAPI.get().getIngredientFromItem(stack.getItem());
                if (!ingredient.isEmpty() && ingredient.getRenderer() != null) state.entries.add(new Entry(ingredient.getRenderer().get(), 0));
            }
        }
        for (int i = 0; i < tileEntityIn.getSlots().getSlots(); i++) {
            var stack = tileEntityIn.getSlots().getStackInSlot(i);
            if (!stack.isEmpty()) {
                var ingredient = com.buuz135.sushigocrafting.api.impl.FoodAPI.get().getIngredientFromItem(stack.getItem());
                if (!ingredient.isEmpty() && ingredient.getRenderer() != null) state.entries.add(new Entry(ingredient.getRenderer().get(), tileEntityIn.getWeightTracker().getWeights().get(i)));
            }
        }
    }

    @Override
    public void submit(State state, PoseStack matrix, net.minecraft.client.renderer.SubmitNodeCollector bufferIn, net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        matrix.pushPose();
        Direction facing = state.facing;
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
        for (var entry : state.entries) {
            var offset = entry.renderer.render(matrix, bufferIn, entry.weight, state.lightCoords, 0);
            matrix.translate(0, offset.getSecond(), offset.getFirst());
        }

        matrix.popPose();
    }
}
