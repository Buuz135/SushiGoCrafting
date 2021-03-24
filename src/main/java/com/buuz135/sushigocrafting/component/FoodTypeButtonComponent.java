package com.buuz135.sushigocrafting.component;

import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.client.gui.FoodTypeSelectionButtonAddon;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public abstract class FoodTypeButtonComponent extends ButtonComponent {

    private final IFoodType type;
    private Supplier<InventoryComponent> component;

    public FoodTypeButtonComponent(IFoodType type, int posX, int posY, int sizeX, int sizeY) {
        super(posX, posY, sizeX, sizeY);
        this.type = type;
    }

    public IFoodType getType() {
        return type;
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new FoodTypeSelectionButtonAddon(this, getSelected()));
    }

    public Supplier<InventoryComponent> getComponent() {
        return component;
    }

    public FoodTypeButtonComponent setComponent(Supplier<InventoryComponent> component) {
        this.component = component;
        return this;
    }

    public abstract Supplier<String> getSelected();
}
