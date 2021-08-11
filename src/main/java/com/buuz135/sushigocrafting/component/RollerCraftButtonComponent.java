package com.buuz135.sushigocrafting.component;

import com.buuz135.sushigocrafting.client.gui.RollerCraftButtonAddon;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.button.ButtonComponent;

import java.util.Collections;
import java.util.List;

public class RollerCraftButtonComponent extends ButtonComponent {

    public RollerCraftButtonComponent(int posX, int posY, int sizeX, int sizeY) {
        super(posX, posY, sizeX, sizeY);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new RollerCraftButtonAddon(this));
    }

}
