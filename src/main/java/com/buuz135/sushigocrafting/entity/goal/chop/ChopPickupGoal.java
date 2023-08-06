package com.buuz135.sushigocrafting.entity.goal.chop;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.buuz135.sushigocrafting.entity.goal.PickupGoal;
import com.buuz135.sushigocrafting.entity.goal.WorkType;
import com.buuz135.sushigocrafting.proxy.SushiContent;
import com.buuz135.sushigocrafting.recipe.CuttingBoardRecipe;
import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.List;

public class ChopPickupGoal extends PickupGoal {
    public ChopPickupGoal(ItamaeCatEntity entity) {
        super(entity, WorkType.CHOP, () -> {
            var workStation = entity.level.getBlockEntity(entity.getWorkOnPosition());
            if (workStation instanceof CuttingBoardTile cuttingBoardTile) {
                return cuttingBoardTile.getInput().getStackInSlot(0).isEmpty();
            }
            return false;
        });
    }

    @Override
    public boolean onDestinationReached() {
        if (getTicksTryingToWork() > 20) {
            var tile = catEntity.level.getBlockEntity(catEntity.getTakeFromPosition());
            if (tile != null) {
                tile.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(iItemHandler -> {
                    List<CuttingBoardRecipe> recipes = (List<CuttingBoardRecipe>) RecipeUtil.getRecipes(catEntity.level, SushiContent.RecipeTypes.CUTTING_BOARD.get());
                    for (int i = 0; i < iItemHandler.getSlots(); i++) {
                        ItemStack itemStack = iItemHandler.getStackInSlot(i);
                        if (itemStack.isEmpty()) continue;
                        for (CuttingBoardRecipe recipe : recipes) {
                            if (recipe.input.test(itemStack)) {
                                catEntity.setHoldingStack(iItemHandler.extractItem(i, 64, false));
                                return;
                            }
                        }
                    }
                });
            }
            return true;
        }
        return false;
    }
}
