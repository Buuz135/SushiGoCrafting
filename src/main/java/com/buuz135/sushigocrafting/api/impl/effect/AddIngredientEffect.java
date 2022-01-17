package com.buuz135.sushigocrafting.api.impl.effect;

import com.buuz135.sushigocrafting.api.IIngredientEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.function.Supplier;

public class AddIngredientEffect implements IIngredientEffect {

    private final Supplier<MobEffect> effect;
    private final int duration;
    private final int level;

    public AddIngredientEffect(Supplier<MobEffect> effect, int durationSeconds, int level) {
        this.effect = effect;
        this.duration = durationSeconds * 20;
        this.level = level;
    }

    @Override
    public void accept(List<MobEffectInstance> effects) {
        effects.add(new MobEffectInstance(effect.get(), duration, level, false, false));
    }

    @Override
    public int getPriority() {
        return 0;
    }

    public Supplier<MobEffect> getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }
}
