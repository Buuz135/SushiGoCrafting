package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.entity.model.ShrimpModel;
import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class ShrimpRenderer extends MobRenderer<ShrimpEntity, ShrimpModel> {
    private static final ResourceLocation SHRIMP_LOCATION = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/shrimp.png");

    public ShrimpRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ShrimpModel(), 0.3F);
    }

    @Override
    public ResourceLocation getEntityTexture(ShrimpEntity entity) {
        return SHRIMP_LOCATION;
    }


    @Override
    protected void applyRotations(ShrimpEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = MathHelper.sin(0.3F * ageInTicks);
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f));
        if (!entityLiving.isInWater()) {
            matrixStackIn.translate((double) 0.1F, (double) 0.1F, (double) -0.1F);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
        }
    }
}
