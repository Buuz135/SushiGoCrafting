package com.buuz135.sushigocrafting.api;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public interface IIngredientEffect {

    void accept(List<MobEffectInstance> effects);

    int getPriority();
}
