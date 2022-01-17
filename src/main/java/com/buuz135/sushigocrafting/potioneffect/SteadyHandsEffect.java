package com.buuz135.sushigocrafting.potioneffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.awt.*;

public class SteadyHandsEffect extends MobEffect {

    public SteadyHandsEffect() {
        super(MobEffectCategory.BENEFICIAL, Color.ORANGE.getRGB());
    }
}
