package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.entity.TunaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TunaRenderer extends MobRenderer<TunaEntity, CodModel<TunaEntity>> {
    private static final ResourceLocation TUNA_LOCATION = ResourceLocation.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "textures/entity/tuna.png");

    public TunaRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CodModel<>(renderManagerIn.bakeLayer(ModelLayers.COD)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(TunaEntity entity) {
        return TUNA_LOCATION;
    }

    @Override
    protected void setupRotations(TunaEntity entity, PoseStack matrixStackIn, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, matrixStackIn, bob, yBodyRot, partialTick, scale);
        float f = Mth.sin(0.3F * bob);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(f));
        if (!entity.isInWater()) {
            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
