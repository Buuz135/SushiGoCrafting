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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
            names.add(Component.literal("" + ChatFormatting.GOLD + Component.translatable("text.sushigocrafting.perfect").getString()));
        } else if (Math.abs(negative) == positive) {
            names.add(Component.literal("" + ChatFormatting.DARK_GREEN + Component.translatable("text.sushigocrafting.weirdly_balanced").getString()));
        }
        if (Math.abs(negative) < positive) {
            names.add(Component.literal("" + ChatFormatting.RED + Component.translatable("text.sushigocrafting.almost_hollow").getString()));
        }
        if (Math.abs(negative) > positive) {
            names.add(Component.literal("" + ChatFormatting.RED + Component.translatable("text.sushigocrafting.overflowing").getString()));
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        tooltip.add(Component.literal("" + ChatFormatting.GRAY + Component.translatable("itemGroup.ingredients").getString() + ": "));
        for (int i = 0; i < ingredientList.size(); i++) {
            if (!ingredientList.get(i).isEmpty()) {
                String line = ChatFormatting.GRAY + " - " + Component.translatable(ingredientList.get(i).getItem().getDescriptionId()).getString();
                if (stack.has(SushiDataComponent.FOOD_WEIGHTS)) {
                    line += " " + getWeightText(stack.get(SushiDataComponent.FOOD_WEIGHTS).get(i));
                }
                tooltip.add(Component.literal(line));
            }
        }
        if (stack.has(SushiDataComponent.FOOD_SPICES)) {
            CompoundTag compoundNBT = stack.get(SushiDataComponent.FOOD_SPICES);
            for (String name : compoundNBT.getAllKeys()) {
                IFoodIngredient foodIngredient = FoodAPI.get().getIngredientFromName(name);
                if (!foodIngredient.isEmpty())
                    tooltip.add(Component.literal(ChatFormatting.GRAY + " - " + Component.translatable(foodIngredient.getItem().getDescriptionId()).getString()));
            }
        }
        boolean hasShift = Screen.hasShiftDown();
        boolean hasAlt = Screen.hasAltDown();
        //hasShift = hasAlt = true;
        Info info = new Info(stack, hasShift);
        tooltip.addAll(getTagsFrom(info.getNegative(), info.getPositive()));
        tooltip.add(Component.literal(""));
        if (hasShift) {
            if (info.getEffectInstances().size() > 0) {
                tooltip.add(Component.literal("" + ChatFormatting.DARK_AQUA + Component.translatable("text.sushigocrafting.effects").getString() + ":"));
                if (hasAlt) {
                    tooltip.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + Component.translatable("text.sushigocrafting.hunger").getString() + ": " + ChatFormatting.WHITE + (int) info.getHunger()));
                    tooltip.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + Component.translatable("text.sushigocrafting.saturation").getString() + ": " + ChatFormatting.WHITE + info.getSaturation()));
                }
                info.getEffectInstances().forEach(effectInstance -> tooltip.add(Component.literal(ChatFormatting.YELLOW + " - " + ChatFormatting.GOLD + Component.translatable(effectInstance.getDescriptionId()).getString() + ChatFormatting.DARK_AQUA + " (" + ChatFormatting.WHITE + effectInstance.getDuration() / 20 + ChatFormatting.YELLOW + "s" + ChatFormatting.DARK_AQUA + ", " + ChatFormatting.YELLOW + "Level " + ChatFormatting.WHITE + (effectInstance.getAmplifier() + 1) + ChatFormatting.DARK_AQUA + ")")));
            }
        } else {
            tooltip.add(Component.literal(ChatFormatting.YELLOW + "" + Component.translatable("text.sushigocrafting.hold").getString() + ChatFormatting.GOLD + " " + ChatFormatting.ITALIC + "<" + Component.translatable("key.keyboard.left.shift").getString() + ">" + ChatFormatting.RESET + ChatFormatting.YELLOW + Component.translatable("text.sushigocrafting.sushi_effect").getString()));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entity) {
        if (entity instanceof Player player) {
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

    @Override
    public @org.jetbrains.annotations.Nullable FoodProperties getFoodProperties(ItemStack stack, @org.jetbrains.annotations.Nullable LivingEntity entity) {
        var info = new Info(stack, true);
        return new FoodProperties.Builder().nutrition((int) Math.floor(info.getHunger())).saturationModifier(info.getSaturation()).build();
    }

    public static class Info {

        private final ItemStack stack;
        private final List<MobEffectInstance> effectInstances;
        private final float saturation;
        private final float hunger;
        private ModifyIngredientEffect modifyIngredientEffect;
        private int positive, negative = 0;

        public Info(ItemStack stack, boolean calculateEffects) {
            this.stack = stack;
            this.effectInstances = new ArrayList<>();
            this.modifyIngredientEffect = null;
            if (stack.has(SushiDataComponent.FOOD_WEIGHTS)) {
                for (int i : stack.get(SushiDataComponent.FOOD_WEIGHTS)) {
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
            this.saturation = (foodItem.getIngredientList().stream().map(IFoodIngredient::getSaturationValue).mapToInt(Integer::intValue).sum() * getFoodModifierValue(negative, positive)) / (foodItem.ingredientList.size() + 2);
            if (calculateEffects) {
                List<IFoodIngredient> foodIngredients = new ArrayList<>(foodItem.getIngredientList());
                if (stack.has(SushiDataComponent.FOOD_SPICES)) {
                    CompoundTag compoundNBT = stack.get(SushiDataComponent.FOOD_SPICES);
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
