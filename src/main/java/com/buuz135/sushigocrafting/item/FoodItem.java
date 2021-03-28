package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.util.TextUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        if (Math.abs(negative) < positive) {
            names.add(new StringTextComponent(TextFormatting.RED + "Almost Hollow"));
        }
        if (Math.abs(negative) > positive) {
            names.add(new StringTextComponent(TextFormatting.RED + "Overflowing"));
        }
        return names;
    }

    public static ModifyIngredientEffect getModifierFrom(int negative, int positive) {
        if (negative == 0 && positive == 0) {
            return new ModifyIngredientEffect(2, 1);
        } else if (Math.abs(negative) == positive) {
            return new ModifyIngredientEffect(1.2f, 0);
        }
        if (Math.abs(negative) < positive) {
            return new ModifyIngredientEffect(0.8f, 0);
        }
        if (Math.abs(negative) > positive) {
            return new ModifyIngredientEffect(0.8f, 0);
        }
        return new ModifyIngredientEffect(1, 0);
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
        ModifyIngredientEffect effect = null;
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
            effect = getModifierFrom(negative, positive);
        }
        tooltip.add(new StringTextComponent(""));
        if (Screen.hasShiftDown()) {
            List<EffectInstance> effectInstances = new ArrayList<>();
            ingredientList.stream().map(IFoodIngredient::getEffect).filter(Objects::nonNull).sorted(Comparator.comparingInt(IIngredientEffect::getPriority)).forEach(iIngredientEffect -> iIngredientEffect.accept(effectInstances));
            if (effect != null) effect.accept(effectInstances);
            if (effectInstances.size() > 0) {
                tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Effects:"));
                effectInstances.forEach(effectInstance -> tooltip.add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + effectInstance.getPotion().getDisplayName().getString() + TextFormatting.DARK_AQUA + " (" + TextFormatting.WHITE + effectInstance.getDuration() + TextFormatting.YELLOW + "s" + TextFormatting.DARK_AQUA + ", " + TextFormatting.YELLOW + "Level " + TextFormatting.WHITE + (effectInstance.getAmplifier() + 1) + TextFormatting.DARK_AQUA + ")")));
            }
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Hold " + TextFormatting.GOLD + "" + TextFormatting.ITALIC + "<Shift>" + TextFormatting.RESET + TextFormatting.YELLOW + " for sushi effect"));
        }
    }

    @Override
    public boolean isFood() {
        return true;
    }

    @Nullable
    @Override
    public Food getFood() {
        return Foods.APPLE; //Todo change
    }
}
