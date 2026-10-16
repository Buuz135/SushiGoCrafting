package com.buuz135.sushigocrafting.network;

import com.buuz135.sushigocrafting.client.gui.PerfectionToast;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;


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
    protected void handleServerMessage(IPayloadContext context) {
        throw new UnsupportedOperationException("Capability sync is client-bound");
    }

    @Override
    public void handleClientMessage(IPayloadContext context) {
        context.enqueueWork(this::handle);
    }

    public void handle() {
        var data = Minecraft.getInstance().player.getData(SushiContent.AttachmentTypes.SUSHI_WEIGHT_DISCOVERY);
        data.deserialize(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, Minecraft.getInstance().level.registryAccess(), capability));
        if (!itemStack.isEmpty() && !itemStack.getItem().equals(Items.STONE)) {
            PerfectionToast toast = new PerfectionToast(itemStack, Component.literal(ChatFormatting.DARK_AQUA + "You have discovered a"), Component.literal(ChatFormatting.DARK_AQUA + "new perfect weight!"), false);
            Minecraft.getInstance().getToastManager().addToast(toast);
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
