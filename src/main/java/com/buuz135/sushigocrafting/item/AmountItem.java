package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.List;

public class AmountItem extends SushiItem {

    public static final String NBT_AMOUNT = "Amount";

    private final int minAmount;
    private final int maxAmount;
    private final int maxCombineAmount;

    public AmountItem(Properties properties, String category, int minAmount, int maxAmount, int maxCombineAmount, boolean foodHurts) {
        super(properties.food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), foodHurts ? 0.6f : 0.01f).build()), category);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.maxCombineAmount = maxCombineAmount;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt(NBT_AMOUNT, worldIn.random.nextInt(maxAmount - minAmount) + minAmount);
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(NBT_AMOUNT);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) stack.getOrCreateTag().getInt(NBT_AMOUNT) * 13.0F / (float) this.maxCombineAmount);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.YELLOW.getRGB();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.hasTag()) {
            tooltip.add(new TextComponent(ChatFormatting.GRAY + "Amount: " + stack.getTag().getInt(NBT_AMOUNT) + "/" + maxCombineAmount + " gr.")); //TODO Localize
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt(NBT_AMOUNT, maxAmount / 2);
            items.add(stack);
        }
    }

    public int getCurrentAmount(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_AMOUNT);
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getMaxCombineAmount() {
        return maxCombineAmount;
    }

    public ItemStack withAmount(int amount) {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putInt(NBT_AMOUNT, amount);
        return stack;
    }

    public ItemStack random(Player entity, Level world) {
        int extra = 0;
        if (entity != null && entity.hasEffect(SushiContent.Effects.STEADY_HANDS.get())) {
            extra += (entity.getEffect(SushiContent.Effects.STEADY_HANDS.get()).getAmplifier() + 1) * getMinAmount();
        }
        return withAmount(Math.min(getMaxCombineAmount(), extra + world.random.nextInt(getMaxAmount() - getMinAmount()) + getMinAmount()));
    }

    public void consume(IFoodIngredient ingredient, ItemStack stack, int amountLevel) {
        int amount = (int) (stack.getOrCreateTag().getInt(NBT_AMOUNT) - ingredient.getDefaultAmount() * (amountLevel + 1) / 5D);
        stack.getOrCreateTag().putInt(NBT_AMOUNT, amount);
        if (amount <= 0) {
            stack.shrink(1);
        }
    }

    public boolean canConsume(IFoodIngredient ingredient, ItemStack stack, int amountLevel) {
        int amount = (int) (ingredient.getDefaultAmount() * (amountLevel + 1) / 5D);
        return !stack.isEmpty() && stack.getOrCreateTag().getInt(NBT_AMOUNT) >= amount;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), this.getEatingSound(), SoundSource.NEUTRAL, 1.0F, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);
            ((Player) entityLiving).getFoodData().eat(stack.getItem().getFoodProperties().getNutrition(), stack.getItem().getFoodProperties().getSaturationModifier());
            for (Pair<MobEffectInstance, Float> pair : stack.getItem().getFoodProperties().getEffects()) {
                if (!worldIn.isClientSide && pair.getFirst() != null && worldIn.random.nextFloat() < pair.getSecond()) {
                    entityLiving.addEffect(new MobEffectInstance(pair.getFirst()));
                }
            }
            if (!((Player) entityLiving).getAbilities().instabuild) {
                consume(FoodAPI.get().getIngredientFromItem(this), stack, 6);
            }
        }
        return stack;
    }

}
