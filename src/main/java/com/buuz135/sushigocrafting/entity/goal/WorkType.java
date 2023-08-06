package com.buuz135.sushigocrafting.entity.goal;

public enum WorkType {
    FREE(false, null),

    CHOP(false, null),
    CHOP_DEPOSIT(true, CHOP);

    private final boolean isDeposit;
    private final WorkType callback;

    WorkType(boolean isDeposit, WorkType callback) {
        this.isDeposit = isDeposit;
        this.callback = callback;
    }

    public boolean isDeposit() {
        return isDeposit;
    }

    public WorkType getCallback() {
        return callback;
    }
}
