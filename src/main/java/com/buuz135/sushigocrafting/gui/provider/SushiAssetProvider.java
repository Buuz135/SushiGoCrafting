package com.buuz135.sushigocrafting.gui.provider;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

public class SushiAssetProvider implements IAssetProvider {

    public static SushiAssetProvider INSTANCE = new SushiAssetProvider();
    private static ResourceLocation TEXTURE = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/gui/background.png");

    private final IAsset ROLLER_WEIGHT_PICKER_BG = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(0, 0, 4, 18);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_WEIGHT_PICKER_POINTER = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(4, 0, 4, 3);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    @Nullable
    @Override
    public <T extends IAsset> T getAsset(IAssetType<T> iAssetType) {
        if (iAssetType == SushiAssetTypes.ROLLER_WEIGHT_PICKER_BG)
            return iAssetType.castOrDefault(ROLLER_WEIGHT_PICKER_BG);
        if (iAssetType == SushiAssetTypes.ROLLER_WEIGHT_PICKER_POINTER)
            return iAssetType.castOrDefault(ROLLER_WEIGHT_PICKER_POINTER);
        return DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(iAssetType);
    }
}
