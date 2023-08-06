package com.buuz135.sushigocrafting.entity.goal.chop;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.buuz135.sushigocrafting.entity.goal.WorkType;
import com.buuz135.sushigocrafting.entity.goal.WorkstationWorkGoal;
import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;

public class ChopWorkstationWorkGoal extends WorkstationWorkGoal {
    public ChopWorkstationWorkGoal(ItamaeCatEntity entity) {
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
        if (getTicksTryingToWork() > 40) {
            var workStation = catEntity.level.getBlockEntity(catEntity.getWorkOnPosition());
            if (workStation instanceof CuttingBoardTile cuttingBoardTile) {
                cuttingBoardTile.processRecipe(catEntity);
                cuttingBoardTile.syncObject(cuttingBoardTile.getInput());
                catEntity.setWorkType(WorkType.CHOP_DEPOSIT);
                return true;
            }
        }
        return false;
    }
}
