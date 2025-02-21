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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
    protected void handleMessage(IPayloadContext context) {
        context.enqueueWork(this::handle);
    }

    @OnlyIn(Dist.CLIENT)
    public void handle() {
        var data = Minecraft.getInstance().player.getData(SushiContent.AttachmentTypes.SUSHI_WEIGHT_DISCOVERY);
        data.deserializeNBT(null, capability);
        if (!itemStack.isEmpty() && !itemStack.getItem().equals(Items.STONE)) {
            PerfectionToast toast = new PerfectionToast(itemStack, Component.literal(ChatFormatting.DARK_AQUA + "" + Component.translatable("text.sushigoocrafting.discovered_a")), Component.literal(ChatFormatting.DARK_AQUA + "" + Component.translatable("text.sushigocrafting.perfect_weight")), false);
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
