package com.buuz135.sushigocrafting.cap;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public interface ISushiWeightDiscovery extends ValueIOSerializable {

    void requestUpdate(ServerPlayer entity, ItemStack discovery);

    boolean hasDiscovery(String discovery);

    int getDiscovery(String discovery);

    void setDiscovery(String discovery, int value);

}
