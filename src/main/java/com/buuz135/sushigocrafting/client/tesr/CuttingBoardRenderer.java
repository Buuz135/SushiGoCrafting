package com.buuz135.sushigocrafting.client.tesr;

import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardTile> {

    public CuttingBoardRenderer() {

    }

    @Override
    public void render(CuttingBoardTile tileEntityIn, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntityIn.getInput().getStackInSlot(0).isEmpty()) {
            matrix.pushPose();
            Direction facing = tileEntityIn.getFacingDirection();
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
            Random random = new Random(tileEntityIn.getClick() + tileEntityIn.getBlockPos().asLong());
            matrix.mulPose(Axis.YN.rotationDegrees(random.nextInt(15) - random.nextInt(15)));
            matrix.mulPose(Axis.XP.rotationDegrees(90));
            matrix.scale(0.5f, 0.5f, 0.5f);

            Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityIn.getInput().getStackInSlot(0), ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrix, bufferIn, tileEntityIn.getLevel(), 0);
            matrix.popPose();
        }
    }
}
