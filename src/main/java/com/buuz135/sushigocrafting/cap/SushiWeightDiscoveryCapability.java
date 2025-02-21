package com.buuz135.sushigocrafting.cap;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SushiWeightDiscoveryCapability implements ISushiWeightDiscovery {

    private final Map<String, Integer> discoveryLevels;

    public SushiWeightDiscoveryCapability() {
        this.discoveryLevels = new HashMap<>();
    }

    @Override
    public void requestUpdate(ServerPlayer entity, ItemStack discovery, HolderLookup.Provider provider) {
        SushiGoCrafting.NETWORK.sendTo(new CapabilitySyncMessage(serializeNBT(provider), discovery), entity);
    }

    @Override
    public boolean hasDiscovery(String discovery) {
        return discoveryLevels.containsKey(discovery);
    }

    @Override
    public int getDiscovery(String discovery) {
        return discoveryLevels.getOrDefault(discovery, -1);
    }

    @Override
    public void setDiscovery(String discovery, int value) {
        discoveryLevels.put(discovery, value);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundNBT = new CompoundTag();
        for (String name : this.discoveryLevels.keySet()) {
            compoundNBT.putInt(name, this.discoveryLevels.get(name));
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.discoveryLevels.clear();
        for (String name : nbt.getAllKeys()) {
            this.discoveryLevels.put(name, nbt.getInt(name));
        }
    }

}
