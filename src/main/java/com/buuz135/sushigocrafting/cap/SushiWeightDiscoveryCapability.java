package com.buuz135.sushigocrafting.cap;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.buuz135.sushigocrafting.network.CapabilitySyncMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SushiWeightDiscoveryCapability implements ISushiWeightDiscovery {

    @CapabilityInject(ISushiWeightDiscovery.class)
    public static Capability<ISushiWeightDiscovery> CAPABILITY = null;

    private Map<String, Integer> discoveryLevels;

    public SushiWeightDiscoveryCapability() {
        this.discoveryLevels = new HashMap<>();
    }

    @Override
    public void requestUpdate(ServerPlayerEntity entity) {
        SushiGoCrafting.NETWORK.get().sendTo(new CapabilitySyncMessage(serializeNBT()), entity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
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
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        for (String name : this.discoveryLevels.keySet()) {
            compoundNBT.putInt(name, this.discoveryLevels.get(name));
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.discoveryLevels.clear();
        for (String name : nbt.keySet()) {
            this.discoveryLevels.put(name, nbt.getInt(name));
        }
    }

    public static class Storage implements Capability.IStorage<ISushiWeightDiscovery> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ISushiWeightDiscovery> capability, ISushiWeightDiscovery instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<ISushiWeightDiscovery> capability, ISushiWeightDiscovery instance, Direction side, INBT nbt) {
            instance.deserializeNBT((CompoundNBT) nbt);
        }
    }
}
