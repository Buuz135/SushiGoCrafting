package com.buuz135.sushigocrafting.client.gui.provider;

import com.buuz135.sushigocrafting.SushiGoCrafting;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.api.client.assets.types.IBackgroundAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

public class RollerAssetProvider implements IAssetProvider {

    public static RollerAssetProvider INSTANCE = new RollerAssetProvider();
    private static ResourceLocation TEXTURE = new ResourceLocation(SushiGoCrafting.MOD_ID, "textures/gui/roller.png");

    private final IAsset SLOT = new IAsset() {
        @Override
        public Rectangle getArea() {
            return new Rectangle(1, 185, 18, 18);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_WEIGHT_PICKER_BG = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(1, 223, 4, 18);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_WEIGHT_PICKER_POINTER = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(5, 223, 4, 3);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_WEIGHT_PERFECT_POINTER = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(9, 223, 2, 1);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final Point HOTBAR_POS = new Point(8, 160);
    private final Point INV_POS = new Point(8, 102);
    private final IBackgroundAsset BACKGROUND = new IBackgroundAsset() {
        public Point getInventoryPosition() {
            return RollerAssetProvider.this.INV_POS;
        }

        public Point getHotbarPosition() {
            return RollerAssetProvider.this.HOTBAR_POS;
        }

        public Rectangle getArea() {
            return new Rectangle(0, 0, 176, 184);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_TYPE_BG = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(1, 204, 18, 18);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    private final IAsset ROLLER_TYPE_BG_OVER = new IAsset() {
        public Rectangle getArea() {
            return new Rectangle(20, 204, 18, 18);
        }

        public ResourceLocation getResourceLocation() {
            return TEXTURE;
        }
    };

    @Nullable
    @Override
    public <T extends IAsset> T getAsset(IAssetType<T> iAssetType) {
        if (iAssetType == AssetTypes.BACKGROUND)
            return iAssetType.castOrDefault(this.BACKGROUND);
        if (iAssetType == SushiAssetTypes.ROLLER_WEIGHT_PICKER_BG)
            return iAssetType.castOrDefault(ROLLER_WEIGHT_PICKER_BG);
        if (iAssetType == SushiAssetTypes.ROLLER_WEIGHT_PICKER_POINTER)
            return iAssetType.castOrDefault(ROLLER_WEIGHT_PICKER_POINTER);
        if (iAssetType == SushiAssetTypes.ROLLER_TYPE_BG)
            return iAssetType.castOrDefault(ROLLER_TYPE_BG);
        if (iAssetType == SushiAssetTypes.ROLLER_TYPE_BG_OVER)
            return iAssetType.castOrDefault(ROLLER_TYPE_BG_OVER);
        if (iAssetType == AssetTypes.SLOT)
            return iAssetType.castOrDefault(this.SLOT);
        if (iAssetType == SushiAssetTypes.ROLLER_WEIGHT_PERFECT_POINTER)
            return iAssetType.castOrDefault(ROLLER_WEIGHT_PERFECT_POINTER);
        return null;
        //return DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(iAssetType);
    }
}
