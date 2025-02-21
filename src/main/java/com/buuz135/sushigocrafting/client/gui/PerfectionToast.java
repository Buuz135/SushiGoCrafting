package com.buuz135.sushigocrafting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class PerfectionToast implements Toast {

    private final Component title;
    private final Component subtitle;
    private final ItemStack display;
    private Toast.Visibility visibility = Toast.Visibility.SHOW;
    private long lastDelta;
    private float displayedProgress;

    public PerfectionToast(ItemStack stack, Component titleComponent, @Nullable Component subtitleComponent, boolean drawProgressBar) {
        this.title = titleComponent;
        this.subtitle = subtitleComponent;
        this.display = stack;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent p_230444_2_, long p_230444_3_) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);
        //p_230444_2_.render(guiGraphics);
        guiGraphics.blitSprite(ResourceLocation.withDefaultNamespace("toast/tutorial"), 0, 0, this.width(), this.height());
        guiGraphics.renderItem(display, 9, 9);
        if (this.subtitle == null) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.title, 30, 12, -11534256, false);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, this.title, 30, 7, -11534256, false);
            guiGraphics.drawString(Minecraft.getInstance().font, this.subtitle, 30, 18, -16777216, false);
        }
        return this.visibility;
    }

    public void hide() {
        this.visibility = Visibility.HIDE;
    }
}
