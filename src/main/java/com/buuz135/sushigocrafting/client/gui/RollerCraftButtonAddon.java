package com.buuz135.sushigocrafting.client.gui;

import com.buuz135.sushigocrafting.client.gui.provider.SushiAssetTypes;
import com.buuz135.sushigocrafting.component.RollerCraftButtonComponent;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.addon.BasicButtonAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class RollerCraftButtonAddon extends BasicButtonAddon {


    public RollerCraftButtonAddon(RollerCraftButtonComponent buttonComponent) {
        super(buttonComponent);

    }

    @Override
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        super.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        IAsset asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG_OVER);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(SushiContent.Items.ROLLER.get()), guiX + getPosX(), guiY + getPosY() - 4);
    }

    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5F, 1.0F, Minecraft.getInstance().player.getPosition()));
        if (screen instanceof ContainerScreen && ((ContainerScreen) screen).getContainer() instanceof ILocatable) {
            ILocatable locatable = (ILocatable) ((ContainerScreen) screen).getContainer();
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("Button", button);
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), this.getButton().getId(), nbt));
        }
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
        return Arrays.asList(new StringTextComponent("Roll"), new StringTextComponent(TextFormatting.DARK_GRAY + "*Left Click to make 1*"), new StringTextComponent(TextFormatting.DARK_GRAY + "*Right Click to make 64*"));
    }

    @Override
    public RollerCraftButtonComponent getButton() {
        return (RollerCraftButtonComponent) super.getButton();
    }
}
