package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.util.TextUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FoodItem extends SushiItem {

    public static final String WEIGHTS_TAG = "Weights";

    private final List<IFoodIngredient> ingredientList;
    private final IFoodType type;

    public FoodItem(Properties properties, IFoodType type) {
        super(properties, type.getName());
        this.type = type;
        this.ingredientList = new ArrayList<>();
    }

    public List<IFoodIngredient> getIngredientList() {
        return ingredientList;
    }

    public IFoodType getType() {
        return type;
    }

    public static String getWeightText(int weight) {
        if (weight < 0) {
            return (weight == -1 ? TextFormatting.RED : TextFormatting.DARK_RED) + TextUtil.ARROW_DOWN;
        }
        if (weight > 0) {
            return (weight == 1 ? TextFormatting.RED : TextFormatting.DARK_RED) + TextUtil.ARROW_UP;
        }
        return TextFormatting.GOLD + TextUtil.PERFECT;
    }

    public static List<ITextComponent> getTagsFrom(int negative, int positive) {
        List<ITextComponent> names = new ArrayList<>();
        if (negative == 0 && positive == 0) {
            names.add(new StringTextComponent(TextFormatting.GOLD + "Perfect"));
        } else if (Math.abs(negative) == positive) {
            names.add(new StringTextComponent(TextFormatting.DARK_GREEN + "Wierdly Balanced"));
        }
        if (Math.abs(negative) > positive) {
            names.add(new StringTextComponent(TextFormatting.RED + "Almost Hollow"));
        }
        if (Math.abs(negative) < positive) {
            names.add(new StringTextComponent(TextFormatting.RED + "Overflowing"));
        }
        return names;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Ingredients: "));
        for (int i = 0; i < ingredientList.size(); i++) {
            if (!ingredientList.get(i).isEmpty()) {
                String line = TextFormatting.GRAY + " - " + new TranslationTextComponent(ingredientList.get(i).getItem().getTranslationKey()).getString();
                if (stack.hasTag()) {
                    line += " " + getWeightText(stack.getTag().getIntArray(WEIGHTS_TAG)[i]);
                }
                tooltip.add(new StringTextComponent(line));
            }
        }
        if (stack.hasTag()) {
            int negative = 0;
            int positive = 0;
            for (int i : stack.getTag().getIntArray(WEIGHTS_TAG)) {
                if (i < 0) {
                    negative += i;
                }
                if (i > 0) {
                    positive += i;
                }
            }
            tooltip.addAll(getTagsFrom(negative, positive));
        }

    }
}
