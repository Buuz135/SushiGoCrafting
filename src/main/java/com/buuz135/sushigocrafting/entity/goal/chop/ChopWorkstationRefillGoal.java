package com.buuz135.sushigocrafting.entity.goal.chop;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import com.buuz135.sushigocrafting.entity.goal.WorkType;
import com.buuz135.sushigocrafting.entity.goal.WorkstationRefillGoal;
import com.buuz135.sushigocrafting.tile.machinery.CuttingBoardTile;

public class ChopWorkstationRefillGoal extends WorkstationRefillGoal {

    public ChopWorkstationRefillGoal(ItamaeCatEntity entity) {
        super(entity, WorkType.CHOP);
    }

    @Override
    public boolean onDestinationReached() {
        if (getTicksTryingToWork() > 20) {
            var workStation = catEntity.level.getBlockEntity(catEntity.getWorkOnPosition());
            if (workStation instanceof CuttingBoardTile cuttingBoardTile && cuttingBoardTile.accepts(catEntity.getHoldingStack())) {
                cuttingBoardTile.getInput().insertItem(0, catEntity.getHoldingStack().copy(), false);
                catEntity.getHoldingStack().setCount(0);
                cuttingBoardTile.syncObject(cuttingBoardTile.getInput());
                return true;
            }
        }
        return false;
    }
}
