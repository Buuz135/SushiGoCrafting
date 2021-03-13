package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import java.awt.*;

public class AcquiredTasteEffect extends Effect {

    static {
        EventManager.forge(LivingEntityUseItemEvent.Finish.class)
                .filter(finish -> finish.getItem().isFood() && finish.getEntityLiving().isPotionActive(SushiContent.Effects.ACQUIRED_TASTE.get()))
                .process(finish -> {
                    EffectInstance instance = finish.getEntityLiving().getActivePotionEffect(SushiContent.Effects.ACQUIRED_TASTE.get());
                    if (!finish.getEntityLiving().world.isRemote() && finish.getEntityLiving() instanceof PlayerEntity) {
                        Food food = finish.getItem().getItem().getFood();
                        int amplifier = instance.getAmplifier() + 1;
                        ((PlayerEntity) finish.getEntityLiving()).getFoodStats().addStats(food.getHealing() * (amplifier / 10), food.getSaturation() * (amplifier / 10F));
                    }
                }).subscribe();
    }

    public AcquiredTasteEffect() {
        super(EffectType.BENEFICIAL, Color.CYAN.getRGB());
    }
}
