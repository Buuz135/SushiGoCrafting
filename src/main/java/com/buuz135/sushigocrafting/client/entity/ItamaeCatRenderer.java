package com.buuz135.sushigocrafting.client.entity;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.client.entity.model.ItamaeCatModel;
import com.buuz135.sushigocrafting.client.entity.model.layer.ItamaeCatHoldingLayer;
import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class ItamaeCatRenderer extends MobRenderer<ItamaeCatEntity, ItamaeCatModel> {

    public static final ResourceLocation TABBY = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_tabby.png");
    public static final ResourceLocation BLACK = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_black.png");
    public static final ResourceLocation RED = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_red.png");
    public static final ResourceLocation SIAMESE = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_siamese.png");
    public static final ResourceLocation BRITISH_SHORTHAIR = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_british_shorthair.png");
    public static final ResourceLocation CALICO = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_calico.png");
    public static final ResourceLocation PERSIAN = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_persian.png");
    public static final ResourceLocation RAGDOLL = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_ragdoll.png");
    public static final ResourceLocation WHITE = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_white.png");
    public static final ResourceLocation JELLIE = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_jellie.png");
    public static final ResourceLocation ALL_BLACK = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/entity/cat/itamae_all_black.png");

    public ItamaeCatRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ItamaeCatModel(renderManagerIn.bakeLayer(new ModelLayerLocation(new ResourceLocation(SushiGoCrafting.MOD_ID, "itamae_cat"), "main"))), 0.4F);
        this.addLayer(new ItamaeCatHoldingLayer(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    protected void scale(ItamaeCatEntity p_115314_, PoseStack p_115315_, float p_115316_) {
        super.scale(p_115314_, p_115315_, p_115316_);
        p_115315_.scale(0.8f, 0.8f, 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(ItamaeCatEntity entity) {
        if (entity.getCatVariant().equals(CatVariant.TABBY)) {
            return TABBY;
        }
        if (entity.getCatVariant().equals(CatVariant.BLACK)) {
            return BLACK;
        }
        if (entity.getCatVariant().equals(CatVariant.RED)) {
            return RED;
        }
        if (entity.getCatVariant().equals(CatVariant.SIAMESE)) {
            return SIAMESE;
        }
        if (entity.getCatVariant().equals(CatVariant.BRITISH_SHORTHAIR)) {
            return BRITISH_SHORTHAIR;
        }
        if (entity.getCatVariant().equals(CatVariant.CALICO)) {
            return CALICO;
        }
        if (entity.getCatVariant().equals(CatVariant.PERSIAN)) {
            return PERSIAN;
        }
        if (entity.getCatVariant().equals(CatVariant.RAGDOLL)) {
            return RAGDOLL;
        }
        if (entity.getCatVariant().equals(CatVariant.WHITE)) {
            return WHITE;
        }
        if (entity.getCatVariant().equals(CatVariant.JELLIE)) {
            return JELLIE;
        }
        return ALL_BLACK;
    }

    @Override
    protected void setupRotations(ItamaeCatEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = entityLiving.getLieDownAmount(partialTicks);
        if (f > 0.0F) {
            matrixStackIn.translate((double) (0.4F * f), (double) (0.15F * f), (double) (0.1F * f));
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.rotLerp(f, 0.0F, 90.0F)));
            BlockPos blockpos = entityLiving.blockPosition();

            for (Player player : entityLiving.level.getEntitiesOfClass(Player.class, (new AABB(blockpos)).inflate(2.0D, 2.0D, 2.0D))) {
                if (player.isSleeping()) {
                    matrixStackIn.translate((double) (0.15F * f), 0.0D, 0.0D);
                    break;
                }
            }
        }

    }
}
