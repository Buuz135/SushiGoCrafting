package com.buuz135.sushigocrafting.entity.goal;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import net.minecraft.core.BlockPos;

public abstract class WorkstationRefillGoal extends GoToPosGoal {
    public WorkstationRefillGoal(ItamaeCatEntity entity, WorkType workType) {
        super(entity, () -> entity.getWorkType() == workType && !entity.getHoldingStack().isEmpty(), () -> true);
    }

    @Override
    public BlockPos getDestination() {
        return catEntity.getWorkOnPosition();
    }


}
