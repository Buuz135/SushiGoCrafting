package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.entity.TunaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TunaRenderer extends MobRenderer<TunaEntity, CodModel<TunaEntity>> {
    private static final ResourceLocation TUNA_LOCATION = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/tuna.png");

    public TunaRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CodModel<>(renderManagerIn.bakeLayer(ModelLayers.COD)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(TunaEntity entity) {
        return TUNA_LOCATION;
    }

    @Override
    protected void setupRotations(TunaEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * Mth.sin(0.6F * ageInTicks);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));
        if (!entityLiving.isInWater()) {
            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        }

    }
}
