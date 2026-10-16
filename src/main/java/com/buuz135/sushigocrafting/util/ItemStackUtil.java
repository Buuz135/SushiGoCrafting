package com.buuz135.sushigocrafting.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemStackUtil {

    @OnlyIn(Dist.CLIENT)
    public static void renderItemIntoGUI(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.item(stack, x, y);
    }
}
