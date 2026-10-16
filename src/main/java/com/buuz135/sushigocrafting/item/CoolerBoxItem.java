package com.buuz135.sushigocrafting.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.util.ProblemReporter;

import java.util.function.Consumer;

public class CoolerBoxItem extends BlockItem {

    public CoolerBoxItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltip, tooltipFlag);
        if (!stack.has(SushiDataComponent.TILE.get()) || Minecraft.getInstance().level == null) {
            return;
        }
        var tileData = stack.get(SushiDataComponent.TILE.get());
        if (!tileData.contains("input")) {
            return;
        }
        NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(TagValueInput.create(ProblemReporter.DISCARDING, Minecraft.getInstance().level.registryAccess(), tileData.getCompoundOrEmpty("input")), contents);
        int shown = 0;
        int total = 0;
        for (ItemStack itemStack : contents) {
            if (!itemStack.isEmpty()) {
                ++total;
                if (shown < 5) {
                    MutableComponent line = itemStack.getHoverName().copy();
                    line.append(" x").append(String.valueOf(itemStack.getCount())).withStyle(ChatFormatting.DARK_AQUA);
                    tooltip.accept(line);
                    ++shown;
                }
            }
        }
        if (total > shown) {
            tooltip.accept(Component.translatable("container.shulkerBox.more", total - shown).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_AQUA));
        }
    }
}
