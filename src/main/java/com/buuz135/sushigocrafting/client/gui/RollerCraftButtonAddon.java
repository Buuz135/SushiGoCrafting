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
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class RollerCraftButtonAddon extends BasicButtonAddon {


    public RollerCraftButtonAddon(RollerCraftButtonComponent buttonComponent) {
        super(buttonComponent);

    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        super.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        IAsset asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG_OVER);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(new ItemStack(SushiContent.Items.ROLLER.get()), guiX + getPosX(), guiY + getPosY() - 4);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen && ((AbstractContainerScreen) screen).getMenu() instanceof ILocatable) {
            if (!isMouseOver(mouseX - ((AbstractContainerScreen<?>) screen).getGuiLeft(), mouseY - ((AbstractContainerScreen<?>) screen).getGuiTop()))
                return false;
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 0.2F, 1.0F, Minecraft.getInstance().level.getRandom(),Minecraft.getInstance().player.blockPosition()));
            ILocatable locatable = (ILocatable) ((AbstractContainerScreen) screen).getMenu();
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("Button", button);
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), this.getButton().getId(), nbt));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public List<Component> getTooltipLines() {
        return Arrays.asList(Component.literal("Roll"), Component.literal(ChatFormatting.DARK_GRAY + "*Left Click to make 1*"), Component.literal(ChatFormatting.DARK_GRAY + "*Right Click to make 64*"));
    }

    @Override
    public RollerCraftButtonComponent getButton() {
        return (RollerCraftButtonComponent) super.getButton();
    }
}
