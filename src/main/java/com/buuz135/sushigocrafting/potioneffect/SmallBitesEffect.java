package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
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
                    if (finish.getEntityLiving() instanceof ServerPlayerEntity && finish.getEntityLiving().world.rand.nextInt(9) <= instance.getAmplifier() + 1) {
                        ServerPlayerEntity player = (ServerPlayerEntity) finish.getEntityLiving();
                        ItemStack stack = finish.getResultStack().copy();
                        stack.setCount(1);
                        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.2f, 1);
                        ItemHandlerHelper.giveItemToPlayer(player, stack);
                        player.sendSlotContents(player.container, player.inventory.currentItem + 36, player.inventory.getStackInSlot(player.inventory.currentItem));
                    }
                }).subscribe();
    }

    public SmallBitesEffect() {
        super(EffectType.BENEFICIAL, Color.GREEN.getRGB());
    }
}
