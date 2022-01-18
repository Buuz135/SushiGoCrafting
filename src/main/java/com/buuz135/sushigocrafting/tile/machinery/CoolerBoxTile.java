package com.buuz135.sushigocrafting.tile.machinery;

import com.buuz135.sushigocrafting.item.AmountItem;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CombineAmountItemRecipe;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class CoolerBoxTile extends ActiveTile<CoolerBoxTile> {

    @Save
    private final InventoryComponent<CoolerBoxTile> input;

    public CoolerBoxTile(BlockPos pos, BlockState state) {
        super(SushiContent.Blocks.COOLER_BOX.get(), pos, state);
        this.addInventory(this.input = new InventoryComponent<CoolerBoxTile>("input", 44, 20, 5 * 4)
                .setRange(5, 4)
                .setInputFilter((stack, integer) -> stack.getItem() instanceof AmountItem)
        );
    }

    @Override
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        InteractionResult type = super.onActivated(player, hand, facing, hitX, hitY, hitZ);
        if (!type.shouldSwing()) {
            openGui(player);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, CoolerBoxTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (this.level.getGameTime() % 10 == 0) {
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

    @Nonnull
    @Override
    public CoolerBoxTile getSelf() {
        return this;
    }

    public InventoryComponent<CoolerBoxTile> getInput() {
        return input;
    }
}
