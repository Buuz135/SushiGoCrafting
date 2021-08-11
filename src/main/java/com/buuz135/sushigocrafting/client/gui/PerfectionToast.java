package com.buuz135.sushigocrafting.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class PerfectionToast implements IToast {

    private final ITextComponent title;
    private final ITextComponent subtitle;
    private IToast.Visibility visibility = IToast.Visibility.SHOW;
    private long lastDelta;
    private float displayedProgress;
    private ItemStack display;

    public PerfectionToast(ItemStack stack, ITextComponent titleComponent, @Nullable ITextComponent subtitleComponent, boolean drawProgressBar) {
        this.title = titleComponent;
        this.subtitle = subtitleComponent;
        this.display = stack;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack p_230444_1_, ToastGui p_230444_2_, long p_230444_3_) {
        p_230444_2_.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        p_230444_2_.blit(p_230444_1_, 0, 0, 0, 96, this.func_230445_a_(), this.func_238540_d_());
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(display, 9, 9);
        if (this.subtitle == null) {
            p_230444_2_.getMinecraft().fontRenderer.drawText(p_230444_1_, this.title, 30.0F, 12.0F, -11534256);
        } else {
            p_230444_2_.getMinecraft().fontRenderer.drawText(p_230444_1_, this.title, 30.0F, 7.0F, -11534256);
            p_230444_2_.getMinecraft().fontRenderer.drawText(p_230444_1_, this.subtitle, 30.0F, 18.0F, -16777216);
        }
        return this.visibility;
    }

    public void hide() {
        this.visibility = IToast.Visibility.HIDE;
    }
}
