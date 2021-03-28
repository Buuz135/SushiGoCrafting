package com.buuz135.sushigocrafting.api.impl.effect;

import com.buuz135.sushigocrafting.api.IIngredientEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.List;
import java.util.function.Supplier;

public class AddIngredientEffect implements IIngredientEffect {

    private final Supplier<Effect> effect;
    private final int duration;
    private final int level;

    public AddIngredientEffect(Supplier<Effect> effect, int duration, int level) {
        this.effect = effect;
        this.duration = duration;
        this.level = level;
    }

    @Override
    public void accept(List<EffectInstance> effects) {
        effects.add(new EffectInstance(effect.get(), duration, level, false, false));
    }

    @Override
    public int getPriority() {
        return 0;
    }

    public Supplier<Effect> getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }
}
