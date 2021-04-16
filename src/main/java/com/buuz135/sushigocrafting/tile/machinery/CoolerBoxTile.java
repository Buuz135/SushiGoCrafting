package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class CoolerBoxTile extends ActiveTile<CoolerBoxTile> {

    @Save
    private InventoryComponent<CoolerBoxTile> input;

    public CoolerBoxTile() {
        super(SushiContent.Blocks.COOLER_BOX.get());
        this.addInventory(this.input = new InventoryComponent<CoolerBoxTile>("input", 44, 20, 5 * 4)
                .setRange(5, 4)
                .setInputFilter((stack, integer) -> stack.getItem() instanceof AmountItem)
        );
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
    public void tick() {
        super.tick();
        if (isServer()) {
            if (this.world.getGameTime() % 10 == 0) {
                for (int current = 0; current < input.getSlots(); current++) {
                    for (int other = 0; other < input.getSlots(); other++) {
                        if (current == other || !CombineAmountItemRecipe.stackMatches(input.getStackInSlot(current), input.getStackInSlot(other)))
                            continue;
                        ItemStack result = CombineAmountItemRecipe.getResult(Arrays.asList(input.getStackInSlot(current), input.getStackInSlot(other)));
                        if (!result.isEmpty()) {
                            input.setStackInSlot(current, result);
                            input.setStackInSlot(other, ItemStack.EMPTY);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public CoolerBoxTile getSelf() {
        return this;
    }
}
