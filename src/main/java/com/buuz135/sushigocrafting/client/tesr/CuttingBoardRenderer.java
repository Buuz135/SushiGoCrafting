package com.buuz135.sushigocrafting.client.tesr;

import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class CuttingBoardRenderer extends TileEntityRenderer<CuttingBoardTile> {

    public CuttingBoardRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(CuttingBoardTile tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntityIn.getInput().getStackInSlot(0).isEmpty()) {
            matrix.push();
            Direction facing = tileEntityIn.getFacingDirection();
            switch (facing) {
                case SOUTH:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.rotate(Vector3f.YP.rotationDegrees(180));
                    break;
                case NORTH:
                    matrix.translate(0.5, 0.1, 0.5);
                    break;
                case EAST:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.rotate(Vector3f.YN.rotationDegrees(90));
                    break;
                case WEST:
                    matrix.translate(0.5, 0.1, 0.5);
                    matrix.rotate(Vector3f.YP.rotationDegrees(90));
                    break;
            }
            Random random = new Random(tileEntityIn.getClick() + tileEntityIn.getPos().toLong());
            matrix.rotate(Vector3f.YN.rotationDegrees(random.nextInt(15) - random.nextInt(15)));
            matrix.rotate(Vector3f.XP.rotationDegrees(90));
            matrix.scale(0.5f, 0.5f, 0.5f);

            Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getInput().getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
            matrix.pop();
        }
    }
}
