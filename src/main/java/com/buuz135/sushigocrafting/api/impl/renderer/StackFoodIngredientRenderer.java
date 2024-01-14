package com.buuz135.sushigocrafting.api.impl.renderer;

import com.buuz135.sushigocrafting.api.IFoodIngredientRenderer;
import com.buuz135.sushigocrafting.item.AmountItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class StackFoodIngredientRenderer implements IFoodIngredientRenderer {

    private final RegistryObject<AmountItem> itemStack;
    private final float x;
    private final float y;
    private final float z;
    private final float rotationY;
    private final float rotationZ;
    private ItemStack itemStackCache;


    public StackFoodIngredientRenderer(RegistryObject<AmountItem> stack, float x, float y, float z, float rotationY, float rotationZ) {
        this.itemStack = stack;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;

    }

    @Override
    public Pair<Float, Float> render(PoseStack stack, MultiBufferSource renderTypeBuffer, int weight, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackCache == null) {
            itemStackCache = new ItemStack(itemStack.get());
        }
        stack.pushPose();
        stack.translate(x, y, z);
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.mulPose(Axis.YP.rotationDegrees(rotationY));
        stack.mulPose(Axis.ZP.rotationDegrees(rotationZ));
        stack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStackCache, ItemDisplayContext.GROUND, combinedLightIn, combinedOverlayIn, stack, renderTypeBuffer, Minecraft.getInstance().level, 0);
        stack.popPose();
        return Pair.of(0f, 0f);
    }
}
