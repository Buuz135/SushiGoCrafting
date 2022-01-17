package com.buuz135.sushigocrafting.client.gui;

import com.buuz135.sushigocrafting.api.IFoodIngredient;
import com.buuz135.sushigocrafting.api.IIngredientConsumer;
import com.buuz135.sushigocrafting.api.impl.FoodAPI;
import com.buuz135.sushigocrafting.cap.SushiWeightDiscoveryCapability;
import com.buuz135.sushigocrafting.client.gui.provider.SushiAssetTypes;
import com.buuz135.sushigocrafting.tile.machinery.RollerTile;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.addon.BasicScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class RollerWeightSelectorButtonComponent extends BasicScreenAddon  {

    private final InventoryComponent<RollerTile> inventoryComponent;
    private final int slot;

    public RollerWeightSelectorButtonComponent(InventoryComponent inventoryComponent, int slot) {
        super(0, 0);
        this.inventoryComponent = inventoryComponent;
        this.slot = slot;
    }

    public static void drawBackground(PoseStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int posX, int posY) {
        IAsset asset = iAssetProvider.getAsset(SushiAssetTypes.ROLLER_WEIGHT_PICKER_BG);
        if (asset != null) {
            AssetUtil.drawAsset(matrixStack, screen, asset, guiX + posX, guiY + posY);
        }
    }

    public static void drawForeground(PoseStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int posX, int posY, int weight, int ySize, String type, int slot) {
        IAsset asset = iAssetProvider.getAsset(SushiAssetTypes.ROLLER_WEIGHT_PICKER_POINTER);
        if (asset != null && weight != Integer.MIN_VALUE) {
            AssetUtil.drawAsset(matrixStack, screen, asset, posX, posY + (4 - weight) * (ySize / 4) - 1);
        }
        Minecraft.getInstance().player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> {
            if (iSushiWeightDiscovery.hasDiscovery(type + "-" + slot)) {
                int pos = posY + (4 - iSushiWeightDiscovery.getDiscovery(type + "-" + slot)) * (ySize / 4) - 1;
                AssetUtil.drawAsset(matrixStack, screen, iAssetProvider.getAsset(SushiAssetTypes.ROLLER_WEIGHT_PERFECT_POINTER), posX + 1, pos + 1);
            }
        });
    }

    @Override
    public void drawBackgroundLayer(PoseStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY, float v) {
        drawBackground(matrixStack, screen, iAssetProvider, guiX, guiY, getPosX(), getPosY());
    }

    @Override
    public void drawForegroundLayer(PoseStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY, float v) {
        drawForeground(matrixStack, screen, iAssetProvider, guiX, guiY, getPosX(), getPosY(), getWeight(), getYSize(), getType(), slot);
    }

    @Override
    public int getPosX() {
        return inventoryComponent.getSlotPosition().apply(slot).getKey() + 17;
    }

    @Override
    public int getPosY() {
        return inventoryComponent.getSlotPosition().apply(slot).getValue() - 1;
    }

    @Override
    public int getXSize() {
        return 4;
    }

    @Override
    public int getYSize() {
        return 18;
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
            nbt.putInt("WeightSlot", slot);
            nbt.putInt("Button", button);
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), 100, nbt));
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
        List<Component> lines = new ArrayList<>();
        if (inventoryComponent.getStackInSlot(slot).isEmpty()) {
            lines.add(new TextComponent(NumberFormat.getInstance(Locale.getDefault()).format(((getWeight() + 1) / 5D) * 100) + ChatFormatting.DARK_AQUA + "%" + ChatFormatting.GOLD + " Weight"));
        } else {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(inventoryComponent.getStackInSlot(slot).getItem());
            if (!ingredient.isEmpty()) {
                String unit = ingredient.getIngredientConsumer() == IIngredientConsumer.STACK ? "u" : "gr";
                double amount = ingredient.getIngredientConsumer() == IIngredientConsumer.STACK ? ingredient.getDefaultAmount() * (getWeight() + 1) : ingredient.getDefaultAmount() * (getWeight() + 1) / 5D;
                lines.add(new TextComponent(ChatFormatting.GOLD + "Consumes " + ChatFormatting.WHITE + NumberFormat.getInstance(Locale.getDefault()).format(amount) + ChatFormatting.YELLOW + unit));
            }
        }
        lines.add(new TextComponent(ChatFormatting.DARK_GRAY + "*Left Click to Increase*"));
        lines.add(new TextComponent(ChatFormatting.DARK_GRAY + "*Right Click to Decrease*"));
        return lines;
    }

    public abstract int getWeight();

    public abstract String getType();

}
