package com.buuz135.sushigocrafting.cap;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashMap;
import java.util.Map;

public class SushiWeightDiscoveryCapability implements ISushiWeightDiscovery {

    private final Map<String, Integer> discoveryLevels;

    public SushiWeightDiscoveryCapability() {
        this.discoveryLevels = new HashMap<>();
    }

    @Override
    public void requestUpdate(ServerPlayer entity, ItemStack discovery) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, Integer> entry : this.discoveryLevels.entrySet()) {
            tag.putInt(entry.getKey(), entry.getValue());
        }
        SushiGoCrafting.NETWORK.sendTo(new CapabilitySyncMessage(tag, discovery), entity);
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
    public void serialize(ValueOutput output) {
        CompoundTag compoundNBT = new CompoundTag();
        for (String name : this.discoveryLevels.keySet()) {
            compoundNBT.putInt(name, this.discoveryLevels.get(name));
        }
        output.store(MapCodec.assumeMapUnsafe(CompoundTag.CODEC), compoundNBT);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.discoveryLevels.clear();
        input.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC)).ifPresent(nbt -> {
            for (String name : nbt.getAllKeys()) this.discoveryLevels.put(name, nbt.getIntOr(name, 0));
        });
    }

}
