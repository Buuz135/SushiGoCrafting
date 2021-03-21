package com.buuz135.sushigocrafting.client.entity.model;


import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ShrimpModel extends EntityModel<ShrimpEntity> {
    private final ModelRenderer body1;
    private final ModelRenderer body2;
    private final ModelRenderer body3;
    private final ModelRenderer body4;

    public ShrimpModel() {
        textureWidth = 32;
        textureHeight = 32;

        body1 = new ModelRenderer(this);
        body1.setRotationPoint(0.0F, 22.0F, -3.5F);
        body1.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        body1.setTextureOffset(21, 5).addBox(-1.5F, 2.0F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        body1.setTextureOffset(20, 0).addBox(-1.5F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        body2 = new ModelRenderer(this);
        body2.setRotationPoint(0.0F, 21.0F, -1.5F);
        body2.setTextureOffset(0, 4).addBox(-1.5F, 1.0F, 1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        body2.setTextureOffset(21, 8).addBox(-1.5F, 3.0F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        body3 = new ModelRenderer(this);
        body3.setRotationPoint(0.0F, 22.0F, 7.0F);
        body3.setTextureOffset(0, 8).addBox(-1.0F, 0.0F, -5.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        body3.setTextureOffset(21, 11).addBox(-1.0F, 2.0F, -5.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        body4 = new ModelRenderer(this);
        body4.setRotationPoint(0.0F, 23.0F, 4.5F);
        body4.setTextureOffset(1, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body4.setTextureOffset(21, 15).addBox(-1.5F, 1.0F, 2.0F, 3.0F, 0.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(ShrimpEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = MathHelper.sin(0.3F * ageInTicks);
        body1.rotateAngleY = f * 0.015f;
        body4.rotateAngleX = f * 0.2f;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body1.render(matrixStack, buffer, packedLight, packedOverlay);
        body2.render(matrixStack, buffer, packedLight, packedOverlay);
        body3.render(matrixStack, buffer, packedLight, packedOverlay);
        body4.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}