package com.buuz135.sushigocrafting.client.entity.model;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ItamaeCatModel extends EntityModel<ItamaeCatEntity> {
    public final ModelPart head;
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart back_left_leg;
    private final ModelPart back_right_leg;
    private final ModelPart front_left_leg;
    private final ModelPart front_right_leg;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart body;
    private int state = 1;

    private float lieDownAmount;
    private float lieDownAmountTail;
    private float relaxStateOneAmount;


    public ItamaeCatModel(ModelPart root) {
        this.back_left_leg = root.getChild("back_left_leg");
        this.back_right_leg = root.getChild("back_right_leg");
        this.front_left_leg = root.getChild("front_left_leg");
        this.front_right_leg = root.getChild("front_right_leg");
        this.tail = root.getChild("tail");
        this.tail2 = root.getChild("tail2");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition back_left_leg = partdefinition.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(8, 13).addBox(1.2F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 18.0F, 5.0F));

        PartDefinition back_right_leg = partdefinition.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(8, 13).addBox(-3.2F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 18.0F, 5.0F));

        PartDefinition front_left_leg = partdefinition.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(40, 0).addBox(1.2F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 14.0F, -5.0F));

        PartDefinition front_right_leg = partdefinition.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(40, 0).addBox(-3.2F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 14.0F, -5.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.5F, 9.0F));

        PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.5F, 9.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-1.5F, -0.02F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 15).addBox(-3.05F, -2.0F, 1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(-0.75F))
                .texOffs(48, 18).addBox(-3.05F, -2.0F, -4.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(-0.75F))
                .texOffs(20, 24).addBox(-2.9F, -2.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.75F))
                .texOffs(20, 24).addBox(1.85F, -2.0F, -4.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(-0.75F)), PartPose.offset(0.0F, 15.0F, -9.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 6.1F));

        PartDefinition rotation = body.addOrReplaceChild("rotation", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -8.0F, -2.9F, 4.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 24).addBox(1.5F, 1.0F, -3.4F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(36, 24).addBox(-2.5F, 1.0F, -3.4F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(48, 21).addBox(-1.5F, 1.0F, 2.6F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 21).addBox(-1.5F, 1.0F, -3.4F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, -5.1F, 1.5708F, 0.0F, 0.0F));

        PartDefinition rotation_r1 = rotation.addOrReplaceChild("rotation_r1", CubeListBuilder.create().texOffs(52, 21).addBox(-4.1F, 0.25F, -1.6F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 28).addBox(-4.6F, -2.0F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.95F, 1.0F, 0.1F, 0.2182F, 0.0F, 0.0F));

        PartDefinition rotation_r2 = rotation.addOrReplaceChild("rotation_r2", CubeListBuilder.create().texOffs(60, 23).addBox(-0.35F, -4.4F, -1.6F, 1.0F, 8.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.95F, 1.0F, 0.1F, -0.1745F, 0.0F, 0.0F));

        PartDefinition rotation_r3 = rotation.addOrReplaceChild("rotation_r3", CubeListBuilder.create().texOffs(56, 23).addBox(-0.35F, -4.5F, -0.3F, 1.0F, 8.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.95F, 1.0F, 0.1F, -0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        back_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        back_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        front_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        front_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

    }

    @Override
    public void setupAnim(ItamaeCatEntity p_102618_, float p_103148_, float p_103149_, float p_103150_, float p_103151_, float p_103152_) {
        this.head.xRot = p_103152_ * ((float) Math.PI / 180F);
        this.head.yRot = p_103151_ * ((float) Math.PI / 180F);
        if (this.state != 3) {
            if (this.state == 2) {
                this.back_left_leg.xRot = Mth.cos(p_103148_ * 0.6662F) * p_103149_;
                this.back_right_leg.xRot = Mth.cos(p_103148_ * 0.6662F + 0.3F) * p_103149_;
                this.front_left_leg.xRot = Mth.cos(p_103148_ * 0.6662F + (float) Math.PI + 0.3F) * p_103149_;
                this.front_right_leg.xRot = Mth.cos(p_103148_ * 0.6662F + (float) Math.PI) * p_103149_;
                this.tail2.xRot = 1.7278761F + ((float) Math.PI / 10F) * Mth.cos(p_103148_) * p_103149_;
            } else {
                this.back_left_leg.xRot = Mth.cos(p_103148_ * 0.6662F) * p_103149_;
                this.back_right_leg.xRot = Mth.cos(p_103148_ * 0.6662F + (float) Math.PI) * p_103149_;
                this.front_left_leg.xRot = Mth.cos(p_103148_ * 0.6662F + (float) Math.PI) * p_103149_;
                this.front_right_leg.xRot = Mth.cos(p_103148_ * 0.6662F) * p_103149_;
                if (this.state == 1) {
                    this.tail2.xRot = 1.7278761F + ((float) Math.PI / 4F) * Mth.cos(p_103148_) * p_103149_;
                } else {
                    this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(p_103148_) * p_103149_;
                }
            }
        }
    }

    @Override
    public void prepareMobModel(ItamaeCatEntity entity, float p_102615_, float p_102616_, float p_102617_) {
        //CAT STUFF
        this.lieDownAmount = entity.getLieDownAmount(p_102617_);
        this.lieDownAmountTail = entity.getLieDownAmountTail(p_102617_);
        this.relaxStateOneAmount = entity.getRelaxStateOneAmount(p_102617_);
        if (this.lieDownAmount <= 0.0F) {
            this.head.xRot = 0.0F;
            this.head.zRot = 0.0F;
            this.front_left_leg.xRot = 0.0F;
            this.front_left_leg.zRot = 0.0F;
            this.front_right_leg.xRot = 0.0F;
            this.front_right_leg.zRot = 0.0F;
            this.front_right_leg.x = -1.2F;
            this.back_left_leg.xRot = 0.0F;
            this.back_right_leg.xRot = 0.0F;
            this.back_right_leg.zRot = 0.0F;
            this.back_right_leg.x = -1.1F;
            this.back_right_leg.y = 18.0F;
        }

        //OCELOT STUFF
        this.body.y = 6.0F;
        this.body.z = 6.1f;
        this.body.xRot = 0;
        this.head.y = 15.0F;
        this.head.z = -9.0F;
        this.tail.y = 15.0F;
        this.tail.z = 8.0F;
        this.tail2.y = 20.0F;
        this.tail2.z = 14.0F;
        this.front_left_leg.y = 14.1F;
        this.front_left_leg.z = -5.0F;
        this.front_right_leg.y = 14.1F;
        this.front_right_leg.z = -5.0F;
        this.front_right_leg.x = 1.1F;
        this.back_left_leg.y = 18.0F;
        this.back_left_leg.z = 5.0F;
        this.back_right_leg.y = 18.0F;
        this.back_right_leg.z = 5.0F;
        this.back_right_leg.x = 1.1F;
        this.tail.xRot = 0.9F;
        if (entity.isCrouching()) {
            ++this.body.y;
            this.head.y += 2.0F;
            ++this.tail.y;
            this.tail2.y += -4.0F;
            this.tail2.z += 2.0F;
            this.tail.xRot = ((float) Math.PI / 2F);
            this.tail2.xRot = ((float) Math.PI / 2F);
            this.state = 0;
        } else if (entity.isSprinting()) {
            this.tail2.y = this.tail.y;
            this.tail2.z += 2.0F;
            this.tail.xRot = ((float) Math.PI / 2F);
            this.tail2.xRot = ((float) Math.PI / 2F);
            this.state = 2;
        } else {
            this.state = 1;
        }

        //MORE CAT
        if (entity.isInSittingPose()) {
            this.body.xRot = ((float) Math.PI / -4F);
            this.body.y += 9.5F;
            this.body.z += 4F;
            this.head.y += -3.3F;
            ++this.head.z;
            this.tail.y += 8.0F;
            this.tail.z += -2.0F;
            this.tail2.y += 2.0F;
            this.tail2.z += -0.8F;
            this.tail.xRot = 1.7278761F;
            this.tail2.xRot = 2.670354F;
            this.front_left_leg.xRot = -0.15707964F;
            this.front_left_leg.y = 16.1F;
            this.front_left_leg.z = -7.0F;
            this.front_right_leg.xRot = -0.15707964F;
            this.front_right_leg.y = 16.1F;
            this.front_right_leg.z = -7.0F;
            this.back_left_leg.xRot = (-(float) Math.PI / 2F);
            this.back_left_leg.y = 21.0F;
            this.back_left_leg.z = 1.0F;
            this.back_right_leg.xRot = (-(float) Math.PI / 2F);
            this.back_right_leg.y = 21.0F;
            this.back_right_leg.z = 1.0F;
            this.state = 3;
        }
    }
}