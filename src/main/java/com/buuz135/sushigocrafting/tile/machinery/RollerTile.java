package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.api.FoodHelper;
import com.buuz135.sushigocrafting.api.IFoodType;
import com.buuz135.sushigocrafting.component.FoodTypeButtonComponent;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

public class RollerTile extends ActiveTile<RollerTile> {

    @Save
    private InventoryComponent<RollerTile> slots;
    @Save
    private String selected;

    public RollerTile() {
        super(SushiContent.Blocks.ROLLER.get());
        int i = 1;
        int max = 0;
        for (IFoodType foodType : FoodHelper.getAllFoodTypes()) {
            addButton(new FoodTypeButtonComponent(foodType, -20 * i, 0, 18, 18).setComponent(this::getSlots));
            ++i;
            if (selected == null) selected = foodType.getName();
            if (foodType.getFoodIngredients().size() > max) max = foodType.getFoodIngredients().size();
        }
        slots = new InventoryComponent<>("slots", 0, 0, max);
        slots.setSlotPosition(FoodHelper.getAllFoodTypes().get(0).getSlotPosition());
        addInventory(slots);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        ActionResultType type = super.onActivated(player, hand, facing, hitX, hitY, hitZ);
        if (!type.isSuccess()) {
            openGui(player);
            return ActionResultType.SUCCESS;
        }
        return type;
    }

    @Override
    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        super.handleButtonMessage(id, playerEntity, compound);
        if (compound.contains("Type")) {
            for (IFoodType foodType : FoodHelper.getAllFoodTypes()) {
                if (foodType.getName().equalsIgnoreCase(compound.getString("Type"))) {
                    slots.setSlotPosition(foodType.getSlotPosition());
                    markForUpdate();
                    break;
                }
            }
        }
    }

    @Override
    public RollerTile getSelf() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public InventoryComponent<RollerTile> getSlots() {
        return slots;
    }
}
