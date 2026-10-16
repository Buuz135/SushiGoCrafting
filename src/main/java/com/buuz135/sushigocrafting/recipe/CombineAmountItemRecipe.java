package com.buuz135.sushigocrafting.recipe;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.item.SushiDataComponent;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class CombineAmountItemRecipe extends CustomRecipe {


    public CombineAmountItemRecipe() {
        super();
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
                output.set(SushiDataComponent.AMOUNT.get(), Math.min(((AmountItem) first.getItem()).getMaxCombineAmount(), ((AmountItem) first.getItem()).getCurrentAmount(first) + ((AmountItem) second.getItem()).getCurrentAmount(second)));
                return output;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        List<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemstack = inv.getItem(i);
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
    public ItemStack assemble(CraftingInput inv) {
        List<ItemStack> list = Lists.newArrayList();
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemstack = inv.getItem(i);
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
    public RecipeSerializer<CombineAmountItemRecipe> getSerializer() {
        return SushiContent.RecipeSerializers.COMBINE_AMOUNT.get();
    }
}
