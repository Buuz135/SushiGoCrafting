package com.buuz135.sushigocrafting.network;

import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;

public class CapabilitySyncMessage extends Message {

    private CompoundNBT capability;

    public CapabilitySyncMessage(CompoundNBT capability) {
        this.capability = capability;
    }

    public CapabilitySyncMessage() {

    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> Minecraft.getInstance().player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.deserializeNBT(capability)));
    }
}
