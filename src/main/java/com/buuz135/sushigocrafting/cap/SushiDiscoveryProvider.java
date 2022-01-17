package com.buuz135.sushigocrafting.cap;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SushiDiscoveryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final ISushiWeightDiscovery discovery = new SushiWeightDiscoveryCapability();
    private final LazyOptional<ISushiWeightDiscovery> optional = LazyOptional.of(() -> discovery);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SushiWeightDiscoveryCapability.CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return discovery.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        discovery.deserializeNBT(nbt);
    }
}
