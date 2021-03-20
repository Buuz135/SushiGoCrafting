package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.entity.TunaEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CodModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class TunaRenderer extends MobRenderer<TunaEntity, CodModel<TunaEntity>> {
    private static final ResourceLocation TUNA_LOCATION = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/tuna.png");

    public TunaRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CodModel<>(), 0.3F);
    }

    @Override
    public ResourceLocation getEntityTexture(TunaEntity entity) {
        return TUNA_LOCATION;
    }

    @Override
    protected void applyRotations(TunaEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
        if (!entityLiving.isInWater()) {
            matrixStackIn.translate((double) 0.1F, (double) 0.1F, (double) -0.1F);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
        }

    }
}
