package com.buuz135.sushigocrafting.util;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {

    public static void renderItemIntoGUI(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.item(stack, x, y);
    }
}
