package com.buuz135.sushigocrafting.client.entity.model.layer;

import com.buuz135.sushigocrafting.client.entity.model.ItamaeCatModel;
import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;

public class ItamaeCatHoldingLayer extends RenderLayer<ItamaeCatEntity, ItamaeCatModel> {
    private ItemInHandRenderer itemInHandRenderer;

    public ItamaeCatHoldingLayer(RenderLayerParent<ItamaeCatEntity, ItamaeCatModel> p_117346_, ItemInHandRenderer itemInHandRenderer) {
        super(p_117346_);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource p_117008_, int p_117009_, ItamaeCatEntity p_117010_, float p_117011_, float p_117012_, float p_117013_, float p_117014_, float p_117015_, float p_117016_) {
        boolean flag = p_117010_.isSleeping();
        boolean flag1 = p_117010_.isBaby();
        stack.pushPose();
        if (flag1) {
            float f = 0.75F;
            stack.scale(0.75F, 0.75F, 0.75F);
            stack.translate(0.0D, 0.5D, (double) 0.209375F);
        }

        stack.translate((double) ((this.getParentModel()).head.x / 16.0F), (double) ((this.getParentModel()).head.y / 16.0F), (double) ((this.getParentModel()).head.z / 16.0F));
        float f1 = 0;
        stack.mulPose(Vector3f.ZP.rotation(f1));
        stack.mulPose(Vector3f.YP.rotationDegrees(p_117015_));
        stack.mulPose(Vector3f.XP.rotationDegrees(p_117016_));
        if (p_117010_.isBaby()) {
            if (flag) {
                stack.translate((double) 0.4F, (double) 0.26F, (double) 0.15F);
            } else {
                stack.translate((double) 0.06F, (double) 0.26F, -0.5D);
            }
        } else if (flag) {
            stack.translate((double) 0.46F, (double) 0.26F, (double) 0.22F);
        } else {
            stack.translate((double) 0.06F, (double) 0.27F, -0.5D);
        }

        stack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        if (flag) {
            stack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        }
        stack.translate(-0.05, 0.1, -0.25);
        stack.scale(0.5f, 0.5f, 0.5f);
        ItemStack itemstack = p_117010_.getHoldingStack();
        this.itemInHandRenderer.renderItem(p_117010_, itemstack, ItemTransforms.TransformType.GROUND, false, stack, p_117008_, p_117009_);
        stack.popPose();
    }
}
