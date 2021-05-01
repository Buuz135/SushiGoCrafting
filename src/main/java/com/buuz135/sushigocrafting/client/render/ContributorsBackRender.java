/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2021, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.sushigocrafting.client.render;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.ClientProxy;
import com.hrznstudio.titanium.reward.storage.ClientRewardStorage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ContributorsBackRender extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    private PlayerModel<AbstractClientPlayerEntity> model;

    public ContributorsBackRender(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50926_1_) {
        super(p_i50926_1_);
        this.model = p_i50926_1_.getEntityModel();
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int p_225628_3_, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!ClientRewardStorage.REWARD_STORAGE.getRewards().containsKey(entitylivingbaseIn.getUniqueID())) return;
        if (!ClientRewardStorage.REWARD_STORAGE.getRewards().get(entitylivingbaseIn.getUniqueID()).getEnabled().containsKey(new ResourceLocation(SushiGoCrafting.MOD_ID, "back")))
            return;
        stack.push();
        if (entitylivingbaseIn.isCrouching()) {
            stack.translate(0D, 0.2D, 0D);
            stack.rotate(Vector3f.XP.rotationDegrees((float) (90F / Math.PI)));
        }
        stack.rotate(Vector3f.XP.rotationDegrees(180));
        stack.translate(-0.53, -0.55, -0.37);
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        IBakedModel selected = null;
        switch (ClientRewardStorage.REWARD_STORAGE.getRewards().get(entitylivingbaseIn.getUniqueID()).getEnabled().getOrDefault(new ResourceLocation(SushiGoCrafting.MOD_ID, "back"), "salmon")) {
            case "tuna":
                selected = ClientProxy.TUNA_BACK;
                break;
            default:
                selected = ClientProxy.SALMON_BACK;
                break;
        }
        if (selected != null) {
            Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(stack.getLast(), buffer.getBuffer(RenderType.getCutout()), null, selected, 1f, 1f, 1f, p_225628_3_, OverlayTexture.NO_OVERLAY);
        }
        stack.pop();
    }


}
