package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.item.AmountItem;
import net.minecraft.item.ItemStack;

public interface IIngredientConsumer {

    IIngredientConsumer STACK = (foodIngredient, stack, amountLevel) -> stack.shrink(1);
    IIngredientConsumer WEIGHT = (foodIngredient, stack, amountLevel) -> {
        if (stack.getItem() instanceof AmountItem) {
            ((AmountItem) stack.getItem()).consume(foodIngredient, stack, amountLevel);
        } else {
            stack.shrink(1);
        }
    };

    void consume(FoodIngredient foodIngredient, ItemStack stack, int amountLevel);

}
