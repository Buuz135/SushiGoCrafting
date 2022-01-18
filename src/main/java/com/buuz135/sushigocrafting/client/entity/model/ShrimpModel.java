package com.buuz135.sushigocrafting.client.entity.model;


import com.buuz135.sushigocrafting.entity.ShrimpEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class ShrimpModel extends HierarchicalModel<ShrimpEntity> {
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart root;

    public ShrimpModel(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.body4 = root.getChild("body4");
        //texWidth = 32;
        //texHeight = 32;

        /*
        body1 = new ModelPart(this);
        body1.setPos(0.0F, 22.0F, -3.5F);
        body1.texOffs(0, 0).addBox(-1.5F, 0.0F, 1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        body1.texOffs(21, 5).addBox(-1.5F, 2.0F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        body1.texOffs(20, 0).addBox(-1.5F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        body2 = new ModelPart(this);
        body2.setPos(0.0F, 21.0F, -1.5F);
        body2.texOffs(0, 4).addBox(-1.5F, 1.0F, 1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        body2.texOffs(21, 8).addBox(-1.5F, 3.0F, 1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        body3 = new ModelPart(this);
        body3.setPos(0.0F, 22.0F, 7.0F);
        body3.texOffs(0, 8).addBox(-1.0F, 0.0F, -5.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        body3.texOffs(21, 11).addBox(-1.0F, 2.0F, -5.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        body4 = new ModelPart(this);
        body4.setPos(0.0F, 23.0F, 4.5F);
        body4.texOffs(1, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        body4.texOffs(21, 15).addBox(-1.5F, 1.0F, 2.0F, 3.0F, 0.0F, 2.0F, 0.0F, false);
        */

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        int i = 22;
        partdefinition.addOrReplaceChild("body1", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.5F, 0.0F, 1.0F, 3.0F, 2.0F, 2.0F)
                        .texOffs(21, 5).addBox(-1.5F, 2.0F, 1.0F, 3.0F, 1.0F, 2.0F)
                        .texOffs(20, 0).addBox(-1.5F, 1.0F, -2.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, -3.5F));
        partdefinition.addOrReplaceChild("body2", CubeListBuilder.create()
                        .texOffs(0, 4).addBox(-1.5F, 1.0F, 1.0F, 3.0F, 2.0F, 2.0F)
                        .texOffs(21, 8).addBox(-1.5F, 3.0F, 1.0F, 3.0F, 1.0F, 2.0F),
                PartPose.offset(0.0F, 21.0F, -1.5F));
        partdefinition.addOrReplaceChild("body3", CubeListBuilder.create()
                        .texOffs(0, 8).addBox(-1.0F, 0.0F, -5.5F, 2.0F, 2.0F, 3.0F)
                        .texOffs(21, 11).addBox(-1.0F, 2.0F, -5.5F, 2.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, 22.0F, 7.0F));
        partdefinition.addOrReplaceChild("body4", CubeListBuilder.create()
                        .texOffs(1, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F)
                        .texOffs(21, 15).addBox(-1.5F, 1.0F, 2.0F, 3.0F, 0.0F, 2.0F),
                PartPose.offset(0.0F, 23.0F, 4.5F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(ShrimpEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = Mth.sin(0.3F * ageInTicks);
        body1.yRot = f * 0.015f;
        body4.xRot = f * 0.2f;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body1.render(matrixStack, buffer, packedLight, packedOverlay);
        body2.render(matrixStack, buffer, packedLight, packedOverlay);
        body3.render(matrixStack, buffer, packedLight, packedOverlay);
        body4.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }


}