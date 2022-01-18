package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.api.IIngredientEffect;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.api.impl.effect.ModifyIngredientEffect;
import com.buuz135.sushigocrafting.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
        super(properties.food(new FoodProperties.Builder().build()), type.getName());
        this.type = type;
        this.ingredientList = new ArrayList<>();
    }

    public static String getWeightText(int weight) {
        if (weight < 0) {
            return (weight == -1 ? ChatFormatting.RED : ChatFormatting.DARK_RED) + TextUtil.ARROW_DOWN;
        }
        if (weight > 0) {
            return (weight == 1 ? ChatFormatting.RED : ChatFormatting.DARK_RED) + TextUtil.ARROW_UP;
        }
        return ChatFormatting.GOLD + TextUtil.PERFECT;
    }

    public static List<Component> getTagsFrom(int negative, int positive) {
        List<Component> names = new ArrayList<>();
        if (negative == 0 && positive == 0) {
            names.add(new TextComponent(ChatFormatting.GOLD + "Perfect"));
        } else if (Math.abs(negative) == positive) {
            names.add(new TextComponent(ChatFormatting.DARK_GREEN + "Weirdly Balanced"));
        }
        if (Math.abs(negative) < positive) {
            names.add(new TextComponent(ChatFormatting.RED + "Almost Hollow"));
        }
        if (Math.abs(negative) > positive) {
            names.add(new TextComponent(ChatFormatting.RED + "Overflowing"));
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

    public List<IFoodIngredient> getIngredientList() {
        return ingredientList;
    }

    public IFoodType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponent(ChatFormatting.GRAY + "Ingredients: "));
        for (int i = 0; i < ingredientList.size(); i++) {
            if (!ingredientList.get(i).isEmpty()) {
                String line = ChatFormatting.GRAY + " - " + new TranslatableComponent(ingredientList.get(i).getItem().getDescriptionId()).getString();
                if (stack.hasTag()) {
                    line += " " + getWeightText(stack.getTag().getIntArray(WEIGHTS_TAG)[i]);
                }
                tooltip.add(new TextComponent(line));
            }
        }
        if (stack.hasTag() && stack.getTagElement(FoodItem.SPICES_TAG) != null) {
            CompoundTag compoundNBT = stack.getTagElement(FoodItem.SPICES_TAG);
            for (String name : compoundNBT.getAllKeys()) {
                IFoodIngredient foodIngredient = FoodAPI.get().getIngredientFromName(name);
                if (!foodIngredient.isEmpty())
                    tooltip.add(new TextComponent(ChatFormatting.GRAY + " - " + new TranslatableComponent(foodIngredient.getItem().getDescriptionId()).getString()));
            }
        }
        boolean hasShift = Screen.hasShiftDown();
        boolean hasAlt = Screen.hasAltDown();
        //hasShift = hasAlt = true;
        Info info = new Info(stack, hasShift);
        tooltip.addAll(getTagsFrom(info.getNegative(), info.getPositive()));
        tooltip.add(new TextComponent(""));
        if (hasShift) {
            if (info.getEffectInstances().size() > 0) {
                tooltip.add(new TextComponent(ChatFormatting.DARK_AQUA + "Effects:"));
                if (hasAlt) {
                    tooltip.add(new TextComponent(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + "Hunger: " + ChatFormatting.WHITE + (int) info.getHunger()));
                    tooltip.add(new TextComponent(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + "Saturation: " + ChatFormatting.WHITE + info.getSaturation()));
                }
                info.getEffectInstances().forEach(effectInstance -> tooltip.add(new TextComponent(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + effectInstance.getEffect().getDisplayName().getString() + ChatFormatting.DARK_AQUA + " (" + ChatFormatting.WHITE + effectInstance.getDuration() / 20 + ChatFormatting.YELLOW + "s" + ChatFormatting.DARK_AQUA + ", " + ChatFormatting.YELLOW + "Level " + ChatFormatting.WHITE + (effectInstance.getAmplifier() + 1) + ChatFormatting.DARK_AQUA + ")")));
            }
        } else {
            tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Hold " + ChatFormatting.GOLD + "" + ChatFormatting.ITALIC + "<Shift>" + ChatFormatting.RESET + ChatFormatting.YELLOW + " for sushi effect"));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Info info = new Info(stack, true);
            player.getFoodData().eat((int) info.getHunger(), info.getSaturation());
            info.getEffectInstances().forEach(player::addEffect);
            worldIn.playSound(null, entity.getX(), entity.getY(), entity.getZ(), entity.getEatingSound(stack), SoundSource.NEUTRAL, 1.0F, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);
            if (!(entity instanceof Player) || !((Player) entity).getAbilities().instabuild) {
                stack.shrink(1);
            }
            return stack;
        }
        return entity.eat(worldIn, stack);
    }

    @Nullable
    @Override
    public FoodProperties getFoodProperties() {
        return new FoodProperties.Builder().nutrition(getIngredientList().stream().mapToInt(IFoodIngredient::getHungerValue).sum()).saturationMod(getIngredientList().stream().mapToInt(IFoodIngredient::getSaturationValue).sum()).build();
    }

    public static class Info {

        private final ItemStack stack;
        private final List<MobEffectInstance> effectInstances;
        private ModifyIngredientEffect modifyIngredientEffect;
        private int positive, negative = 0;
        private final float saturation;
        private final float hunger;

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
                if (stack.hasTag() && stack.getTagElement(FoodItem.SPICES_TAG) != null) {
                    CompoundTag compoundNBT = stack.getTagElement(FoodItem.SPICES_TAG);
                    for (String name : compoundNBT.getAllKeys()) {
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

        public List<MobEffectInstance> getEffectInstances() {
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
