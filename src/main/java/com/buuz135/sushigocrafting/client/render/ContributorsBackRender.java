package com.buuz135.sushigocrafting.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import java.util.List;
import net.minecraft.world.entity.Pose;
import com.mojang.math.Axis;

public class ContributorsBackRender extends RenderLayer<AvatarRenderState, PlayerModel> {
    public ContributorsBackRender(RenderLayerParent<AvatarRenderState, PlayerModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState state, float limbSwing, float limbSwingAmount) {
        var owner = state.getRenderData(com.buuz135.sushigocrafting.client.ClientProxy.BACK_OWNER);
        if (owner == null || state.isInvisible) return;
        var rewards = com.hrznstudio.titanium.reward.storage.ClientRewardStorage.REWARD_STORAGE.getRewards().get(owner);
        if (rewards == null) return;
        var backKey = net.minecraft.resources.Identifier.fromNamespaceAndPath(com.buuz135.sushigocrafting.SushiGoCrafting.MOD_ID, "back");
        if (!rewards.getEnabled().containsKey(backKey)) return;
        var back = rewards.getEnabled().get(backKey);
        var model = "tuna".equals(back) ? com.buuz135.sushigocrafting.client.ClientProxy.TUNA_BACK_MODEL : com.buuz135.sushigocrafting.client.ClientProxy.SALMON_BACK_MODEL;
        if (model != null) {
            pose.pushPose();
            if (state.hasPose(Pose.CROUCHING)) {
                pose.translate(0D, 0.2D, 0D);
                pose.mulPose(Axis.XP.rotationDegrees((float) (90F / Math.PI)));
            }
            pose.mulPose(Axis.XP.rotationDegrees(180));
            pose.translate(-0.53, -0.55, -0.37);
            collector.submitBlockModel(pose, RenderTypes.cutoutMovingBlock(), List.of(model), new int[0], light, 0, 0);
            pose.popPose();
        }
    }
}
