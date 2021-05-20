package com.buuz135.sushigocrafting.api;

import com.buuz135.sushigocrafting.item.AmountItem;
import net.minecraft.item.ItemStack;

public interface IIngredientConsumer {

    IIngredientConsumer STACK = new IIngredientConsumer() {
        @Override
        public void consume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel) {
            stack.shrink(amountLevel + 1);
        }

        @Override
        public boolean canConsume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel) {
            return !stack.isEmpty() && stack.getCount() >= (amountLevel + 1);
        }
    };
    IIngredientConsumer WEIGHT = new IIngredientConsumer() {
        @Override
        public void consume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel) {
            if (stack.getItem() instanceof AmountItem) {
                ((AmountItem) stack.getItem()).consume(foodIngredient, stack, amountLevel);
            } else {
                STACK.consume(foodIngredient, stack, amountLevel);
            }
        }

        @Override
        public boolean canConsume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel) {
            if (stack.getItem() instanceof AmountItem) {
                return ((AmountItem) stack.getItem()).canConsume(foodIngredient, stack, amountLevel);
            } else {
                return STACK.canConsume(foodIngredient, stack, amountLevel);
            }
        }
    };

    void consume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel);

    boolean canConsume(IFoodIngredient foodIngredient, ItemStack stack, int amountLevel);

}
