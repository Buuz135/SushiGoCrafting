package com.buuz135.sushigocrafting.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class AmountItem extends SushiItem {

    public static final String NBT_AMOUNT = "Amount";

    private final int minAmount;
    private final int maxAmount;
    private final int maxCombineAmount;

    public AmountItem(Properties properties, String category, int minAmount, int maxAmount, int maxCombineAmount) {
        super(properties, category);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.maxCombineAmount = maxCombineAmount;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt(NBT_AMOUNT, worldIn.rand.nextInt(maxAmount - minAmount) + minAmount);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(NBT_AMOUNT);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - stack.getOrCreateTag().getInt(NBT_AMOUNT) / (double) maxCombineAmount;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Color.YELLOW.getRGB();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.hasTag()) {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Amount: " + stack.getTag().getInt(NBT_AMOUNT) + " gr.")); //TODO Localize
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt(NBT_AMOUNT, maxAmount / 2);
            items.add(stack);
        }
    }

    public int getCurrentAmount(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_AMOUNT);
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMaxCombineAmount() {
        return maxCombineAmount;
    }

    public ItemStack withAmount(int amount) {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt(NBT_AMOUNT, amount);
        return stack;
    }
}
