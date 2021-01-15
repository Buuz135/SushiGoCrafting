package com.buuz135.sushigocrafting.gui.provider;

import com.hrznstudio.titanium.api.client.GenericAssetType;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IAssetType;

public class SushiAssetTypes {

    public static final IAssetType<IAsset> ROLLER_WEIGHT_PICKER_POINTER = new GenericAssetType<>(SushiAssetProvider.INSTANCE::getAsset, IAsset.class);
    public static final IAssetType<IAsset> ROLLER_WEIGHT_PICKER_BG = new GenericAssetType<>(SushiAssetProvider.INSTANCE::getAsset, IAsset.class);

}
