package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.entity.model.ShrimpModel;
import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ShrimpRenderer extends MobRenderer<ShrimpEntity, ShrimpModel> {
    private static final ResourceLocation SHRIMP_LOCATION = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/shrimp.png");

    public ShrimpRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ShrimpModel(renderManagerIn.bakeLayer(new ModelLayerLocation(new ResourceLocation(SushiGoCrafting.MOD_ID, "shrimp"), "main"))), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShrimpEntity entity) {
        return SHRIMP_LOCATION;
    }


    @Override
    protected void setupRotations(ShrimpEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = Mth.sin(0.3F * ageInTicks);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(f));
        if (!entityLiving.isInWater()) {
            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
