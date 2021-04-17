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
import com.hrznstudio.titanium.client.screen.addon.interfaces.IClickable;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.util.AssetUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class RollerWeightSelectorButtonComponent extends BasicScreenAddon implements IClickable {

    private final InventoryComponent<RollerTile> inventoryComponent;
    private final int slot;

    public RollerWeightSelectorButtonComponent(InventoryComponent inventoryComponent, int slot) {
        super(0, 0);
        this.inventoryComponent = inventoryComponent;
        this.slot = slot;
    }

    public static void drawBackground(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int posX, int posY) {
        IAsset asset = iAssetProvider.getAsset(SushiAssetTypes.ROLLER_WEIGHT_PICKER_BG);
        if (asset != null) {
            AssetUtil.drawAsset(matrixStack, screen, asset, guiX + posX, guiY + posY);
        }
    }

    public static void drawForeground(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int posX, int posY, int weight, int ySize, String type, int slot) {
        IAsset asset = iAssetProvider.getAsset(SushiAssetTypes.ROLLER_WEIGHT_PICKER_POINTER);
        if (asset != null && weight != Integer.MIN_VALUE) {
            AssetUtil.drawAsset(matrixStack, screen, asset, posX, posY + (4 - weight) * (ySize / 4) - 1);
        }
        Minecraft.getInstance().player.getCapability(SushiWeightDiscoveryCapability.CAPABILITY).ifPresent(iSushiWeightDiscovery -> {
            if (iSushiWeightDiscovery.hasDiscovery(type + "-" + slot)) {
                int pos = posY + (4 - iSushiWeightDiscovery.getDiscovery(type + "-" + slot)) * (ySize / 4) - 1;
                Screen.fill(matrixStack, posX + 1, pos + 1, posX + 3, pos + 2, new Color(TextFormatting.GOLD.getColor()).getRGB());
            }
        });
    }

    @Override
    public void drawBackgroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY, float v) {
        drawBackground(matrixStack, screen, iAssetProvider, guiX, guiY, getPosX(), getPosY());
    }

    @Override
    public void drawForegroundLayer(MatrixStack matrixStack, Screen screen, IAssetProvider iAssetProvider, int guiX, int guiY, int mouseX, int mouseY) {
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
    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.BLOCK_WOOD_FALL, SoundCategory.PLAYERS, 0.5F, 1.0F, Minecraft.getInstance().player.getPosition()));
        if (screen instanceof ContainerScreen && ((ContainerScreen) screen).getContainer() instanceof ILocatable) {
            ILocatable locatable = (ILocatable) ((ContainerScreen) screen).getContainer();
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("WeightSlot", slot);
            nbt.putInt("Button", button);
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), 100, nbt));
        }
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
        List<ITextComponent> lines = new ArrayList<>();
        if (inventoryComponent.getStackInSlot(slot).isEmpty()) {
            lines.add(new StringTextComponent(NumberFormat.getInstance(Locale.getDefault()).format(((getWeight() + 1) / 5D) * 100) + TextFormatting.DARK_AQUA + "%" + TextFormatting.GOLD + " Weight"));
        } else {
            IFoodIngredient ingredient = FoodAPI.get().getIngredientFromItem(inventoryComponent.getStackInSlot(slot).getItem());
            if (!ingredient.isEmpty()) {
                String unit = ingredient.getIngredientConsumer() == IIngredientConsumer.STACK ? "u" : "gr";
                double amount = ingredient.getIngredientConsumer() == IIngredientConsumer.STACK ? ingredient.getDefaultAmount() * (getWeight() + 1) : ingredient.getDefaultAmount() * (getWeight() + 1) / 5D;
                lines.add(new StringTextComponent(TextFormatting.GOLD + "Consumes " + TextFormatting.WHITE + NumberFormat.getInstance(Locale.getDefault()).format(amount) + TextFormatting.YELLOW + unit));
            }
        }
        lines.add(new StringTextComponent(TextFormatting.DARK_GRAY + "*Left Click to Increase*"));
        lines.add(new StringTextComponent(TextFormatting.DARK_GRAY + "*Right Click to Decrease*"));
        return lines;
    }

    public abstract int getWeight();

    public abstract String getType();

}
