package com.buuz135.sushigocrafting.api.impl.effect;

import com.buuz135.sushigocrafting.api.IIngredientEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class AddIngredientEffect implements IIngredientEffect {

    private final Holder<MobEffect> effect;
    private final int duration;
    private final int level;

    public AddIngredientEffect(Holder<MobEffect> effect, int durationSeconds, int level) {
        this.effect = effect;
        this.duration = durationSeconds * 20;
        this.level = level;
    }

    @Override
    public void accept(List<MobEffectInstance> effects) {
        effects.add(new MobEffectInstance(effect, duration, level, false, false));
    }

    @Override
    public int getPriority() {
        return 0;
    }

    public Holder<MobEffect> getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }
}
