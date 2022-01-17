package com.buuz135.sushigocrafting.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISushiWeightDiscovery extends INBTSerializable<CompoundTag> {

    void requestUpdate(ServerPlayer entity, ItemStack discovery);

    boolean hasDiscovery(String discovery);

    int getDiscovery(String discovery);

    void setDiscovery(String discovery, int value);

}
