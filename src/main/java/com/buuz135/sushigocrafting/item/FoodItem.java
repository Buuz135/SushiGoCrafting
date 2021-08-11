package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.util.TextUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
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
    public static final String SPICES_TAG = "Spices";

    private final List<IFoodIngredient> ingredientList;
    private final IFoodType type;

    public FoodItem(Properties properties, IFoodType type) {
        super(properties.food(new Food.Builder().build()), type.getName());
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
            names.add(new StringTextComponent(TextFormatting.DARK_GREEN + "Weirdly Balanced"));
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

    public static float getFoodModifierValue(int negative, int positive) {
        if (negative == 0 && positive == 0) {
            return 1.25f;
        } else if (Math.abs(negative) == positive) {
            return 0.9f;
        }
        return 0.8f;
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
        if (stack.hasTag() && stack.getChildTag(FoodItem.SPICES_TAG) != null) {
            CompoundNBT compoundNBT = stack.getChildTag(FoodItem.SPICES_TAG);
            for (String name : compoundNBT.keySet()) {
                IFoodIngredient foodIngredient = FoodAPI.get().getIngredientFromName(name);
                if (!foodIngredient.isEmpty())
                    tooltip.add(new StringTextComponent(TextFormatting.GRAY + " - " + new TranslationTextComponent(foodIngredient.getItem().getTranslationKey()).getString()));
            }
        }
        boolean hasShift = Screen.hasShiftDown();
        boolean hasAlt = Screen.hasAltDown();
        //hasShift = hasAlt = true;
        Info info = new Info(stack, hasShift);
        tooltip.addAll(getTagsFrom(info.getNegative(), info.getPositive()));
        tooltip.add(new StringTextComponent(""));
        if (hasShift) {
            if (info.getEffectInstances().size() > 0) {
                tooltip.add(new StringTextComponent(TextFormatting.DARK_AQUA + "Effects:"));
                if (hasAlt) {
                    tooltip.add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + "Hunger: " + TextFormatting.WHITE + (int) info.getHunger()));
                    tooltip.add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + "Saturation: " + TextFormatting.WHITE + info.getSaturation()));
                }
                info.getEffectInstances().forEach(effectInstance -> tooltip.add(new StringTextComponent(TextFormatting.YELLOW + " - " + TextFormatting.GOLD + effectInstance.getPotion().getDisplayName().getString() + TextFormatting.DARK_AQUA + " (" + TextFormatting.WHITE + effectInstance.getDuration() / 20 + TextFormatting.YELLOW + "s" + TextFormatting.DARK_AQUA + ", " + TextFormatting.YELLOW + "Level " + TextFormatting.WHITE + (effectInstance.getAmplifier() + 1) + TextFormatting.DARK_AQUA + ")")));
            }
        } else {
            tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Hold " + TextFormatting.GOLD + "" + TextFormatting.ITALIC + "<Shift>" + TextFormatting.RESET + TextFormatting.YELLOW + " for sushi effect"));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Info info = new Info(stack, true);
            player.getFoodStats().addStats((int) info.getHunger(), info.getSaturation());
            info.getEffectInstances().forEach(player::addPotionEffect);
            worldIn.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.getEatSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.4F);
            if (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).abilities.isCreativeMode) {
                stack.shrink(1);
            }
            return stack;
        }
        return entity.onFoodEaten(worldIn, stack);
    }

    @Nullable
    @Override
    public Food getFood() {
        return new Food.Builder().hunger(getIngredientList().stream().mapToInt(IFoodIngredient::getHungerValue).sum()).saturation(getIngredientList().stream().mapToInt(IFoodIngredient::getSaturationValue).sum()).build();
    }

    public static class Info {

        private final ItemStack stack;
        private final List<EffectInstance> effectInstances;
        private ModifyIngredientEffect modifyIngredientEffect;
        private int positive, negative = 0;
        private float saturation;
        private float hunger;

        public Info(ItemStack stack, boolean calculateEffects) {
            this.stack = stack;
            this.effectInstances = new ArrayList<>();
            this.modifyIngredientEffect = null;
            if (stack.hasTag()) {
                for (int i : stack.getTag().getIntArray(WEIGHTS_TAG)) {
                    if (i < 0) {
                        negative += i;
                    }
                    if (i > 0) {
                        positive += i;
                    }
                }
                this.modifyIngredientEffect = getModifierFrom(negative, positive);
            }
            FoodItem foodItem = (FoodItem) stack.getItem();
            this.hunger = foodItem.getIngredientList().stream().map(IFoodIngredient::getHungerValue).mapToInt(Integer::intValue).sum() * getFoodModifierValue(negative, positive);
            this.saturation = foodItem.getIngredientList().stream().map(IFoodIngredient::getSaturationValue).mapToInt(Integer::intValue).sum() * getFoodModifierValue(negative, positive);
            if (calculateEffects) {
                List<IFoodIngredient> foodIngredients = new ArrayList<>(foodItem.getIngredientList());
                if (stack.hasTag() && stack.getChildTag(FoodItem.SPICES_TAG) != null) {
                    CompoundNBT compoundNBT = stack.getChildTag(FoodItem.SPICES_TAG);
                    for (String name : compoundNBT.keySet()) {
                        IFoodIngredient foodIngredient = FoodAPI.get().getIngredientFromName(name);
                        if (!foodIngredient.isEmpty()) foodIngredients.add(foodIngredient);
                    }
                }
                foodIngredients.stream().map(IFoodIngredient::getEffect).filter(Objects::nonNull).sorted(Comparator.comparingInt(IIngredientEffect::getPriority)).forEach(iIngredientEffect -> iIngredientEffect.accept(effectInstances));
                if (modifyIngredientEffect != null) modifyIngredientEffect.accept(effectInstances);
            }
        }

        public ItemStack getStack() {
            return stack;
        }

        public List<EffectInstance> getEffectInstances() {
            return effectInstances;
        }

        public ModifyIngredientEffect getModifyIngredientEffect() {
            return modifyIngredientEffect;
        }

        public int getPositive() {
            return positive;
        }

        public int getNegative() {
            return negative;
        }

        public float getSaturation() {
            return saturation;
        }

        public float getHunger() {
            return hunger;
        }
    }
}
