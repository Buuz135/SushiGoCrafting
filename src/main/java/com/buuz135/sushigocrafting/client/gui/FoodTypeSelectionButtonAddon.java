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
    public void drawBackgroundLayer(MatrixStack stack, Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        super.drawBackgroundLayer(stack, screen, provider, guiX, guiY, mouseX, mouseY, partialTicks);
        IAsset asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG);
        if (asset != null) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        asset = provider.getAsset(SushiAssetTypes.ROLLER_TYPE_BG_OVER);
        if (asset != null && getButton().getType().getName().equalsIgnoreCase(selected.get())) {
            AssetUtil.drawAsset(stack, screen, asset, guiX + getPosX() - 1, guiY + getPosY() - 1);
        }
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(FoodHelper.REGISTERED.get(getButton().getType().getName()).get(0)), guiX + getPosX(), guiY + getPosY());
    }

    public void handleClick(Screen screen, int guiX, int guiY, double mouseX, double mouseY, int button) {
        Minecraft.getInstance().getSoundHandler().play(new SimpleSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5F, 1.0F, Minecraft.getInstance().player.getPosition()));
        if (screen instanceof ContainerScreen && ((ContainerScreen) screen).getContainer() instanceof ILocatable) {
            ILocatable locatable = (ILocatable) ((ContainerScreen) screen).getContainer();
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Type", getButton().getType().getName());
            getButton().getComponent().get().setSlotPosition(getButton().getType().getSlotPosition());
            for (int i = 0; i < getButton().getComponent().get().getSlots(); i++) {
                getButton().getComponent().get().setSlotToItemStackRender(i, getButton().getType().getSlotStackRender().apply(i));
            }
            Titanium.NETWORK.get().sendToServer(new ButtonClickNetworkMessage(locatable.getLocatorInstance(), this.getButton().getId(), nbt));
            for (int i1 = 0; i1 < getButton().getComponent().get().getSlots(); i1++) {
                getButton().getComponent().get().setSlotLimit(i1, i1 < getButton().getType().getFoodIngredients().size() ? 64 : 0);
            }
        }
    }

    @Override
    public List<ITextComponent> getTooltipLines() {
        return Collections.singletonList(new StringTextComponent(WordUtils.capitalize(getButton().getType().getName())));
    }

    @Override
    public FoodTypeButtonComponent getButton() {
        return (FoodTypeButtonComponent) super.getButton();
    }
}
