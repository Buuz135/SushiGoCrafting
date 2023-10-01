package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import java.awt.*;

public class AcquiredTasteEffect extends MobEffect {

    static {
        EventManager.forge(LivingEntityUseItemEvent.Finish.class)
                .filter(finish -> finish.getItem().isEdible() && finish.getEntity().hasEffect(SushiContent.Effects.ACQUIRED_TASTE.get()))
                .process(finish -> {
                    MobEffectInstance instance = finish.getEntity().getEffect(SushiContent.Effects.ACQUIRED_TASTE.get());
                    if (!finish.getEntity().level().isClientSide() && finish.getEntity() instanceof Player) {
                        FoodProperties food = finish.getItem().getItem().getFoodProperties();
                        int amplifier = instance.getAmplifier() + 1;
                        ((Player) finish.getEntity()).getFoodData().eat(food.getNutrition() * (amplifier / 10), food.getSaturationModifier() * (amplifier / 10F));
                    }
                }).subscribe();
    }

    public AcquiredTasteEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    }
}
