package com.buuz135.sushigocrafting.potioneffect;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.awt.*;

public class SmallBitesEffect extends MobEffect {

    static {
        EventManager.forge(LivingEntityUseItemEvent.Finish.class)
                .filter(finish -> finish.getItem().isEdible() && finish.getEntity().hasEffect(SushiContent.Effects.SMALL_BITES.get()))
                .process(finish -> {
                    MobEffectInstance instance = finish.getEntity().getEffect(SushiContent.Effects.SMALL_BITES.get());
                    if (finish.getEntity() instanceof ServerPlayer && finish.getEntity().level().random.nextInt(9) <= instance.getAmplifier() + 1) {
                        ServerPlayer player = (ServerPlayer) finish.getEntity();
                        ItemStack stack = finish.getResultStack().copy();
                        stack.setCount(1);
                        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f, 1);
                        ItemHandlerHelper.giveItemToPlayer(player, stack);
                    }
                }).subscribe();
    }

    public SmallBitesEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.GREEN.getRGB());
    }
}
