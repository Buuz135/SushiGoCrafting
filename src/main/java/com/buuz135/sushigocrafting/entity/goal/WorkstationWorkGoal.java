package com.buuz135.sushigocrafting.entity.goal;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public abstract class WorkstationWorkGoal extends GoToPosGoal {

    public WorkstationWorkGoal(ItamaeCatEntity entity, WorkType neededWork, Supplier<Boolean> doesWorkstationNeedItem) {
        super(entity, () -> entity.getWorkType() == neededWork && !doesWorkstationNeedItem.get() && entity.getHoldingStack().isEmpty(), () -> true);
    }

    @Override
    public BlockPos getDestination() {
        return this.catEntity.getWorkOnPosition();
    }

}
