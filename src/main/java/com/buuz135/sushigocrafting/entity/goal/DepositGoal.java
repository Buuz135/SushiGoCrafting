package com.buuz135.sushigocrafting.entity.goal;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class DepositGoal extends GoToPosGoal {
    public DepositGoal(ItamaeCatEntity entity) {
        super(entity, () -> !entity.getHoldingStack().isEmpty() && entity.getWorkType().isDeposit(), () -> true);
    }

    @Override
    public BlockPos getDestination() {
        return catEntity.getDepositToPosition();
    }

    @Override
    public boolean onDestinationReached() {
        if (getTicksTryingToWork() > 20) {
            var tile = catEntity.level.getBlockEntity(catEntity.getDepositToPosition());
            if (tile != null) {
                tile.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).ifPresent(iItemHandler -> {
                    for (int i = 0; i < iItemHandler.getSlots(); i++) {
                        var returned = iItemHandler.insertItem(i, catEntity.getHoldingStack(), false);
                        catEntity.setHoldingStack(returned);
                        if (returned.isEmpty()) {
                            catEntity.setWorkType(catEntity.getWorkType().getCallback());
                            return;
                        }
                    }
                });
                return catEntity.getHoldingStack().isEmpty();
            }
        }
        return false;
    }
}
