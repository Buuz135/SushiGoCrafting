package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.google.common.collect.Lists;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class CombineAmountItemRecipe extends SpecialRecipe {

    public static IRecipeSerializer<CombineAmountItemRecipe> SERIALIZER = new SpecialRecipeSerializer<>(CombineAmountItemRecipe::new);

    public CombineAmountItemRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    public static boolean stackMatches(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem() && first.getCount() == 1 && second.getCount() == 1 && first.getItem() instanceof AmountItem && ((AmountItem) first.getItem()).getCurrentAmount(first) + ((AmountItem) second.getItem()).getCurrentAmount(second) <= ((AmountItem) first.getItem()).getMaxCombineAmount();
    }

    public static boolean matches(List<ItemStack> list) {
        return list.size() == 2;
    }

    public static ItemStack getResult(List<ItemStack> list) {
        if (list.size() == 2) {
            ItemStack first = list.get(0);
            ItemStack second = list.get(1);
            if (first.getItem() == second.getItem() && first.getCount() == 1 && second.getCount() == 1 && first.getItem() instanceof AmountItem) {
                ItemStack output = new ItemStack(first.getItem());
                output.getOrCreateTag().putInt(AmountItem.NBT_AMOUNT, Math.min(((AmountItem) first.getItem()).getMaxCombineAmount(), ((AmountItem) first.getItem()).getCurrentAmount(first) + ((AmountItem) second.getItem()).getCurrentAmount(second)));
                return output;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        List<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if (!stackMatches(itemstack, itemstack1)) {
                        return false;
                    }
                }
            }
        }
        return matches(list);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        List<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if (!stackMatches(itemstack, itemstack1)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return getResult(list);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
