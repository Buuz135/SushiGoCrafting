package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.entity.model.ShrimpModel;
import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class ShrimpRenderer extends MobRenderer<ShrimpEntity, ShrimpRenderer.State, ShrimpModel> {
    public static final class State extends LivingEntityRenderState { }
    private static final Identifier SHRIMP_LOCATION = Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "textures/entity/shrimp.png");

    public ShrimpRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ShrimpModel(renderManagerIn.bakeLayer(new ModelLayerLocation(Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "shrimp"), "main"))), 0.3F);
    }

    @Override
    public Identifier getTextureLocation(State entity) {
        return SHRIMP_LOCATION;
    }


    @Override
    protected void setupRotations(State entity, PoseStack matrixStackIn, float bob, float yBodyRot) {
        super.setupRotations(entity, matrixStackIn, bob, yBodyRot);
        float f = Mth.sin(0.3F * bob);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(f));
        if (!entity.isInWater) {
            matrixStackIn.translate(0.1F, 0.1F, -0.1F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }

    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(ShrimpEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isInWater = entity.isInWater();
    }
}
