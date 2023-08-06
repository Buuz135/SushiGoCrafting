package com.buuz135.sushigocrafting.entity.goal;

import com.buuz135.sushigocrafting.entity.ItamaeCatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

public abstract class GoToPosGoal extends Goal {
    public ItamaeCatEntity catEntity;
    public boolean isDone;
    public BlockPos targetPos;
    Supplier<Boolean> canUse;
    Supplier<Boolean> canContinue;
    private int ticksRunning;
    private int ticksTryingToWork;
    private int extendedRange;

    public GoToPosGoal(ItamaeCatEntity entity, Supplier<Boolean> canUse, Supplier<Boolean> canContinue) {
        this.catEntity = entity;
        this.canUse = canUse;
        this.canContinue = canContinue;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.ticksRunning = 0;
        this.ticksTryingToWork = 0;
        this.extendedRange = 0;
    }

    public GoToPosGoal(ItamaeCatEntity entity, Supplier<Boolean> canUse) {
        this(entity, canUse, canUse);
    }

    @Override
    public void stop() {
        super.stop();
        isDone = false;
        targetPos = null;
    }

    @Override
    public void start() {
        super.start();
        isDone = false;
        this.targetPos = getDestination();
        this.ticksRunning = 0;
        this.ticksTryingToWork = 0;
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksRunning++;

        if (targetPos == null)
            return;
        if (this.ticksRunning % 100 == 0 && !isDestinationStillValid(targetPos)) {
            isDone = true;
            return;
        }

        if (catEntity.position().add(0, 0.5, 0).distanceTo(new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5)) <= 1.5D + this.extendedRange && isDestinationStillValid(targetPos)) {
            this.ticksTryingToWork++;
            isDone = onDestinationReached();
            return;
        }

        if (targetPos != null) {
            setPath(targetPos);
        }
    }

    @Override
    public boolean canUse() {
        return canUse.get()
                && !this.catEntity.getWorkOnPosition().equals(BlockPos.ZERO)
                && !this.catEntity.getDepositToPosition().equals(BlockPos.ZERO)
                && !this.catEntity.getTakeFromPosition().equals(BlockPos.ZERO);
    }

    @Override
    public boolean canContinueToUse() {
        return targetPos != null && !isDone && canContinue.get();
    }

    public void setPath(BlockPos pos) {
        catEntity.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.1);
        //starbuncle.addGoalDebug(this, new DebugEvent("path_set", "path set to " + targetPos.toString()));
        if (catEntity.getNavigation().getPath() != null && !catEntity.getNavigation().getPath().canReach()) {
            isDone = true;
            //starbuncle.addGoalDebug(this, new DebugEvent("unreachable", targetPos.toString()));
        }
    }

    public abstract BlockPos getDestination();

    public abstract boolean onDestinationReached();

    public boolean isDestinationStillValid(BlockPos pos) {
        return true;
    }

    public int getTicksTryingToWork() {
        return ticksTryingToWork;
    }
}
