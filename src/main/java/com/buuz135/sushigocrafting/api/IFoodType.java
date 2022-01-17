package com.buuz135.sushigocrafting.api;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

public interface IFoodType {

    List<IFoodIngredient[]> getFoodIngredients();

    int[] getNameIndex();

    String getName();

    Function<Integer, Pair<Integer, Integer>> getSlotPosition();

    Function<Integer, ItemStack> getSlotStackRender();

}
