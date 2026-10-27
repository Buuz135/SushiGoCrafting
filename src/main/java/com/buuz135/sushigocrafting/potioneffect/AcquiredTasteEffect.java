package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.item.FoodItem;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.awt.*;

public class AcquiredTasteEffect extends MobEffect {

    static {
        EventManager.forge(LivingEntityUseItemEvent.Finish.class)
                .filter(finish -> finish.getItem().has(DataComponents.FOOD) && finish.getEntity().hasEffect(SushiContent.Effects.ACQUIRED_TASTE))
                .process(finish -> {
                    MobEffectInstance instance = finish.getEntity().getEffect(SushiContent.Effects.ACQUIRED_TASTE.getDelegate());
                    if (!finish.getEntity().level().isClientSide() && finish.getEntity() instanceof Player player) {
                        int amplifier = instance.getAmplifier() + 1;
                        if (finish.getItem().getItem() instanceof FoodItem) {
                            FoodItem.Info info = new FoodItem.Info(finish.getItem(), true);
                            player.getFoodData().eat((int) info.getHunger() * amplifier / 10, info.getSaturation() * amplifier / 10F);
                        } else {
                            FoodProperties food = finish.getItem().get(DataComponents.FOOD);
                            player.getFoodData().eat(food.nutrition() * amplifier / 10, food.saturation() * amplifier / 10F);
                        }
                    }
                }).subscribe();
    }

    public AcquiredTasteEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    }
}
