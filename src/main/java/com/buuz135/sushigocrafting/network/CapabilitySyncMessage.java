package com.buuz135.sushigocrafting.network;

import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.gui.PerfectionToast;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class CapabilitySyncMessage extends Message {

    private CompoundTag capability;
    private ItemStack itemStack;

    public CapabilitySyncMessage(CompoundTag capability, ItemStack itemStack) {
        this.capability = capability;
        this.itemStack = itemStack;
    }

    public CapabilitySyncMessage() {

    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(this::handle);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle() {
        Minecraft.getInstance().player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> iSushiWeightDiscovery.deserializeNBT(capability));
        if (!itemStack.isEmpty()) {
            PerfectionToast toast = new PerfectionToast(itemStack, new TextComponent(ChatFormatting.DARK_AQUA + "You have discovered a"), new TextComponent(ChatFormatting.DARK_AQUA + "new perfect weight!"), false);
            Minecraft.getInstance().getToasts().addToast(toast);
            new Thread(() -> {
                try {
                    Thread.sleep(5 * 1000);
                    toast.hide();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
