package com.buuz135.sushigocrafting.cap;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISushiWeightDiscovery extends INBTSerializable<CompoundNBT> {

    void requestUpdate(ServerPlayerEntity entity, boolean discovery);

    boolean hasDiscovery(String discovery);

    int getDiscovery(String discovery);

    void setDiscovery(String discovery, int value);

}
