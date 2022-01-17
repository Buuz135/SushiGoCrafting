package com.buuz135.sushigocrafting.client.gui;

import com.buuz135.sushigocrafting.api.impl.FoodHelper;
import com.buuz135.sushigocrafting.client.gui.provider.SushiAssetTypes;
import com.buuz135.sushigocrafting.component.FoodTypeButtonComponent;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.addon.BasicButtonAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class FoodTypeSelectionButtonAddon extends BasicButtonAddon {

    private final Supplier<String> selected;

    public FoodTypeSelectionButtonAddon(FoodTypeButtonComponent buttonComponent, Supplier<String> selected) {
        super(buttonComponent);
        this.selected = selected;
        if (getButton().getType().getName().equalsIgnoreCase(selected.get())) {
            getButton().getComponent().get().setSlotPosition(getButton().getType().getSlotPosition());
            for (int i = 0; i < getButton().getComponent().get().getSlots(); i++) {
                getButton().getComponent().get().setSlotToItemStackRender(i, getButton().getType().getSlotStackRender().apply(i));
            }
        }
    }

    @Override
    public void drawBackgroundLayer(PoseStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        super.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        IAsset asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG_OVER);
        if (asset != null && getButton().getType().getName().equalsIgnoreCase(selected.get())) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(new ItemStack(FoodHelper.REGISTERED.get(getButton().getType().getName()).get(0)), guiX + getPosX(), guiY + getPosY());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen && ((AbstractContainerScreen) screen).getMenu() instanceof ILocatable) {
            if (!isMouseOver(mouseX - ((AbstractContainerScreen<?>) screen).getGuiLeft(), mouseY - ((AbstractContainerScreen<?>) screen).getGuiTop()))
                return false;
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.WOOD_FALL, SoundSource.PLAYERS, 0.5F, 1.0F, Minecraft.getInstance().player.blockPosition()));
            ILocatable locatable = (ILocatable) ((AbstractContainerScreen) screen).getMenu();
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Type", getButton().getType().getName());
            getButton().getComponent().get().setSlotPosition(getButton().getType().getSlotPosition());
            for (int i = 0; i < getButton().getComponent().get().getSlots(); i++) {
                getButton().getComponent().get().setSlotToItemStackRender(i, getButton().getType().getSlotStackRender().apply(i));
            }
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), this.getButton().getId(), nbt));
            for (int i1 = 0; i1 < getButton().getComponent().get().getSlots(); i1++) {
                getButton().getComponent().get().setSlotLimit(i1, i1 < getButton().getType().getFoodIngredients().size() ? 64 : 0);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.getPosX() && mouseX <= this.getPosX() + getXSize() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + getYSize();
    }

    @Override
    public List<Component> getTooltipLines() {
        return Collections.singletonList(new TextComponent(WordUtils.capitalize(getButton().getType().getName())));
    }

    @Override
    public FoodTypeButtonComponent getButton() {
        return (FoodTypeButtonComponent) super.getButton();
    }
}
