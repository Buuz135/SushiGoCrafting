package com.buuz135.sushigocrafting.entity;

import com.buuz135.sushigocrafting.proxy.SushiContent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.CodEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TunaEntity extends CodEntity {

    public TunaEntity(EntityType<? extends CodEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public int getMaxGroupSize() {
        return 5;
    }

    @Override
    protected ItemStack getFishBucket() {
        return new ItemStack(SushiContent.Items.TUNA_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_SALMON_FLOP;
    }
}
