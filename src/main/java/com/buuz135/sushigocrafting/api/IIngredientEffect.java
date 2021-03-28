package com.buuz135.sushigocrafting.api;

import net.minecraft.potion.EffectInstance;

import java.util.List;

public interface IIngredientEffect {

    void accept(List<EffectInstance> effects);

    int getPriority();
}
