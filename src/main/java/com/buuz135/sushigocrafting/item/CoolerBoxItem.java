package com.buuz135.sushigocrafting.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
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
        if (!stack.has(SushiDataComponent.TILE.get()) || context.registries() == null) {
            return;
        }
        var tileData = stack.get(SushiDataComponent.TILE.get());
        if (!tileData.contains("input")) {
            return;
        }
        ItemStacksResourceHandler contents = new ItemStacksResourceHandler(20);
        contents.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, context.registries(), tileData.getCompound("input").orElseThrow()));
        int shown = 0;
        int total = 0;
        for (int slot = 0; slot < contents.size(); slot++) {
            ItemStack itemStack = contents.getResource(slot).toStack(contents.getAmountAsInt(slot));
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
