package com.buuz135.sushigocrafting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class PerfectionToast implements Toast {

    private final Component title;
    private final Component subtitle;
    private Toast.Visibility visibility = Toast.Visibility.SHOW;
    private long lastDelta;
    private float displayedProgress;
    private ItemStack display;

    public PerfectionToast(ItemStack stack, Component titleComponent, @Nullable Component subtitleComponent, boolean drawProgressBar) {
        this.title = titleComponent;
        this.subtitle = subtitleComponent;
        this.display = stack;
    }

    @Override
    public Visibility render(PoseStack p_230444_1_, ToastComponent p_230444_2_, long p_230444_3_) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);
        p_230444_2_.blit(p_230444_1_, 0, 0, 0, 96, this.width(), this.height());
        Minecraft.getInstance().getItemRenderer().renderGuiItem(display, 9, 9);
        if (this.subtitle == null) {
            p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.title, 30.0F, 12.0F, -11534256);
        } else {
            p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.title, 30.0F, 7.0F, -11534256);
            p_230444_2_.getMinecraft().font.draw(p_230444_1_, this.subtitle, 30.0F, 18.0F, -16777216);
        }
        return this.visibility;
    }

    public void hide() {
        this.visibility = Toast.Visibility.HIDE;
    }
}
