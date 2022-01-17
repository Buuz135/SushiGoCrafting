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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class ContributorsBackRender extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private PlayerModel<AbstractClientPlayer> model;

    public ContributorsBackRender(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_i50926_1_) {
        super(p_i50926_1_);
        this.model = p_i50926_1_.getModel();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int p_225628_3_, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!ClientRewardStorage.REWARD_STORAGE.getRewards().containsKey(entitylivingbaseIn.getUUID())) return;
        if (!ClientRewardStorage.REWARD_STORAGE.getRewards().get(entitylivingbaseIn.getUUID()).getEnabled().containsKey(new ResourceLocation(SushiGoCrafting.MOD_ID, "back")))
            return;
        stack.pushPose();
        if (entitylivingbaseIn.isCrouching()) {
            stack.translate(0D, 0.2D, 0D);
            stack.mulPose(Vector3f.XP.rotationDegrees((float) (90F / Math.PI)));
        }
        stack.mulPose(Vector3f.XP.rotationDegrees(180));
        stack.translate(-0.53, -0.55, -0.37);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        BakedModel selected = null;
        switch (ClientRewardStorage.REWARD_STORAGE.getRewards().get(entitylivingbaseIn.getUUID()).getEnabled().getOrDefault(new ResourceLocation(SushiGoCrafting.MOD_ID, "back"), "salmon")) {
            case "tuna":
                selected = ClientProxy.TUNA_BACK;
                break;
            default:
                selected = ClientProxy.SALMON_BACK;
                break;
        }
        if (selected != null) {
            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderType.cutout()), null, selected, 1f, 1f, 1f, p_225628_3_, OverlayTexture.NO_OVERLAY);
        }
        stack.popPose();
    }


}
