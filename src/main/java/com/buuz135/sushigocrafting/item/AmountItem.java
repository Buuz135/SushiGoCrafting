package com.buuz135.sushigocrafting.item;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.api.ISpecialCreativeTabItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.awt.*;
import java.util.List;

public class AmountItem extends SushiItem implements ISpecialCreativeTabItem {


    private final int minAmount;
    private final int maxAmount;
    private final int maxCombineAmount;

    public AmountItem(Properties properties, String category, int minAmount, int maxAmount, int maxCombineAmount, boolean foodHurts) {
        super(properties.food((new FoodProperties.Builder()).nutrition(2).saturationModifier(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), foodHurts ? 0.6f : 0.01f).build()), category);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.maxCombineAmount = maxCombineAmount;
    }

    public static ItemStack combineStacks(ItemStack first, ItemStack second) {
        if (!first.is(second.getItem())) return null;
        if (first.getItem() instanceof AmountItem firstAmount && second.getItem() instanceof AmountItem secondAmount) {
            first.set(SushiDataComponent.AMOUNT, Math.min(firstAmount.getMaxCombineAmount(), firstAmount.getCurrentAmount(first) + secondAmount.getCurrentAmount(second)));
            return first;
        }
        return null;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        if (!stack.has(SushiDataComponent.AMOUNT))
            stack.set(SushiDataComponent.AMOUNT, worldIn.random.nextInt(maxAmount - minAmount) + minAmount);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(SushiDataComponent.AMOUNT);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) stack.getOrDefault(SushiDataComponent.AMOUNT, 0) * 13.0F / (float) this.maxCombineAmount);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.YELLOW.getRGB();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(SushiDataComponent.AMOUNT)) {
            tooltipComponents.add(Component.literal("" + ChatFormatting.GRAY +Component.translatable("text.sushigocrafting.amount")  + stack.get(SushiDataComponent.AMOUNT) + "/" + maxCombineAmount + " gr.")); //TODO Localize
        }
    }

    public int getCurrentAmount(ItemStack stack) {
        return stack.getOrDefault(SushiDataComponent.AMOUNT, 0);
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
        stack.set(SushiDataComponent.AMOUNT, amount);
        return stack;
    }

    public ItemStack random(Player entity, Level world) {
        int extra = 0;
        if (entity != null && entity.hasEffect(SushiContent.Effects.STEADY_HANDS.getDelegate())) {
            extra += (entity.getEffect(SushiContent.Effects.STEADY_HANDS.getDelegate()).getAmplifier() + 1) * getMinAmount();
        }
        return withAmount(Math.min(getMaxCombineAmount(), extra + world.random.nextInt(getMaxAmount() - getMinAmount()) + getMinAmount()));
    }

    public void consume(IFoodIngredient ingredient, ItemStack stack, int amountLevel) {
        int amount = (int) (stack.getOrDefault(SushiDataComponent.AMOUNT, 0) - ingredient.getDefaultAmount() * (amountLevel + 1) / 5D);
        stack.set(SushiDataComponent.AMOUNT, amount);
        if (amount <= 0) {
            stack.shrink(1);
        }
    }

    public boolean canConsume(IFoodIngredient ingredient, ItemStack stack, int amountLevel) {
        int amount = (int) (ingredient.getDefaultAmount() * (amountLevel + 1) / 5D);
        return !stack.isEmpty() && stack.getOrDefault(SushiDataComponent.AMOUNT, 0) >= amount;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), this.getEatingSound(), SoundSource.NEUTRAL, 1.0F, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);
            ((Player) entityLiving).getFoodData().eat(stack.getItem().getFoodProperties(stack, entityLiving));
            for (FoodProperties.PossibleEffect pair : stack.getItem().getFoodProperties(stack, entityLiving).effects()) {
                if (!worldIn.isClientSide && pair.effect() != null && worldIn.random.nextFloat() < pair.probability()) {
                    entityLiving.addEffect(new MobEffectInstance(pair.effect()));
                }
            }
            if (!((Player) entityLiving).getAbilities().instabuild) {
                consume(FoodAPI.get().getIngredientFromItem(this), stack, 6);
            }
        }
        return stack;
    }

    @Override
    public void addToTab(BuildCreativeModeTabContentsEvent event) {
        ItemStack stack = new ItemStack(this);
        stack.set(SushiDataComponent.AMOUNT, maxAmount / 2);
        event.accept(stack);
    }
}
