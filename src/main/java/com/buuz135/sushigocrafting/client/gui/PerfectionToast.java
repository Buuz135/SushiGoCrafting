package com.buuz135.sushigocrafting.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
    public Visibility getWantedVisibility() { return visibility; }

    @Override
    public void update(ToastManager manager, long time) { }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, Font font, long time) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("toast/tutorial"), 0, 0, this.width(), this.height());
        guiGraphics.item(display, 9, 9);
        if (this.subtitle == null) {
            guiGraphics.text(font, this.title, 30, 12, -11534256, false);
        } else {
            guiGraphics.text(font, this.title, 30, 7, -11534256, false);
            guiGraphics.text(font, this.subtitle, 30, 18, -16777216, false);
        }
    }

    public void hide() {
        this.visibility = Visibility.HIDE;
    }
}
