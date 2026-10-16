package com.buuz135.sushigocrafting.client.tesr;

import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardTile, CuttingBoardRenderer.State> {

    public CuttingBoardRenderer() {

    }

    public static final class State extends BlockEntityRenderState {
        private Direction facing;
        private long click;
        private final ItemStackRenderState item = new ItemStackRenderState();
        private boolean hasItem;
    }

    @Override
    public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(CuttingBoardTile tileEntityIn, State state, float partialTicks, net.minecraft.world.phys.Vec3 camera, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(tileEntityIn, state, breakProgress);
        state.facing = tileEntityIn.getFacingDirection();
        state.click = tileEntityIn.getClick();
        var stack = tileEntityIn.getInput().getStackInSlot(0);
        state.hasItem = !stack.isEmpty();
        if (state.hasItem) net.minecraft.client.Minecraft.getInstance().getItemModelResolver().updateForTopItem(state.item, stack, ItemDisplayContext.FIXED, tileEntityIn.getLevel(), null, 0);
    }

    @Override
    public void submit(State state, PoseStack matrix, SubmitNodeCollector bufferIn, CameraRenderState camera) {
        if (state.hasItem) {
            matrix.pushPose();
            Direction facing = state.facing;
            switch (facing) {
                case SOUTH:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.mulPose(Axis.YP.rotationDegrees(180));
                    break;
                case NORTH:
                    matrix.translate(0.5, 0.1, 0.5);
                    break;
                case EAST:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.mulPose(Axis.YN.rotationDegrees(90));
                    break;
                case WEST:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.mulPose(Axis.YP.rotationDegrees(90));
                    break;
            }
            Random random = new Random(state.click + state.blockPos.asLong());
            matrix.mulPose(Axis.YN.rotationDegrees(random.nextInt(15) - random.nextInt(15)));
            matrix.mulPose(Axis.XP.rotationDegrees(90));
            matrix.scale(0.5f, 0.5f, 0.5f);

            state.item.submit(matrix, bufferIn, state.lightCoords, 0, 0);
            matrix.popPose();
        }
    }
}
