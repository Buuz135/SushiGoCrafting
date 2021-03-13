package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.awt.*;

public class SmallBitesEffect extends Effect {

    static {
        EventManager.forge(LivingEntityUseItemEvent.Finish.class)
                .filter(finish -> finish.getItem().isFood() && finish.getEntityLiving().isPotionActive(SushiContent.Effects.SMALL_BITES.get()))
                .process(finish -> {
                    EffectInstance instance = finish.getEntityLiving().getActivePotionEffect(SushiContent.Effects.SMALL_BITES.get());
                    if (!finish.getEntityLiving().world.isRemote() && finish.getEntityLiving() instanceof PlayerEntity && instance.getAmplifier() + 1 <= finish.getEntityLiving().world.rand.nextInt(9)) {
                        ItemStack stack = finish.getItem().copy();
                        stack.setCount(1);
                        ItemHandlerHelper.giveItemToPlayer((PlayerEntity) finish.getEntityLiving(), stack);
                        finish.getEntityLiving().playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }).subscribe();
    }

    public SmallBitesEffect() {
        super(EffectType.BENEFICIAL, Color.GREEN.getRGB());
    }
}
