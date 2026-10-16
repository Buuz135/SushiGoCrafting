package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.entity.TunaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.fish.CodModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class TunaRenderer extends MobRenderer<TunaEntity, TunaRenderer.State, CodModel<TunaRenderer.State>> {
    public static final class State extends LivingEntityRenderState { }
    private static final Identifier TUNA_LOCATION = Identifier.fromNamespaceAndPath(SushiGoCrafting.MOD_ID, "textures/entity/tuna.png");

    public TunaRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CodModel<>(renderManagerIn.bakeLayer(ModelLayers.COD)), 0.3F);
    }

    @Override
    public Identifier getTextureLocation(State entity) {
        return TUNA_LOCATION;
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
    @Override public void extractRenderState(TunaEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isInWater = entity.isInWater();
    }
}
