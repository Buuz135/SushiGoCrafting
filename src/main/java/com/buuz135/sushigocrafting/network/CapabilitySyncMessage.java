package com.buuz135.sushigocrafting.network;

import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class CapabilitySyncMessage extends Message {

    private CompoundNBT capability;
    private boolean hasUpdated;

    public CapabilitySyncMessage(CompoundNBT capability, boolean hasUpdated) {
        this.capability = capability;
        this.hasUpdated = hasUpdated;
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
        if (hasUpdated) {
            TutorialToast toast = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, new StringTextComponent(TextFormatting.DARK_AQUA + "You have discovered a"), new StringTextComponent(TextFormatting.DARK_AQUA + "new perfect weight!"), false);
            Minecraft.getInstance().getToastGui().add(toast);
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
