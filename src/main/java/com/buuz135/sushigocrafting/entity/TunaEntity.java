package com.buuz135.sushigocrafting.entity;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TunaEntity extends Cod {

    public TunaEntity(EntityType<? extends Cod> type, Level worldIn) {
        super(type, worldIn);
    }

    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(SushiContent.Items.TUNA_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }
}
