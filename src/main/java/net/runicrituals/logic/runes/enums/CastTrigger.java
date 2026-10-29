package net.runicrituals.logic.runes.enums;

public enum CastTrigger {

    DELAY(true, true),
    KILL_EXECUTION(false, false),
    BOLT_HIT(true, true);

    private final boolean canCreateMultipleTriggers;
    private final boolean willCreateTrigger;

    CastTrigger(boolean canCreateMultipleTriggers, boolean willCreateTrigger) {
        this.canCreateMultipleTriggers = canCreateMultipleTriggers;
        this.willCreateTrigger = willCreateTrigger;
    }

    public boolean allowedMultiTrigger() {
        return canCreateMultipleTriggers;
    }

    public boolean allowedToCreateTrigger() {
        return willCreateTrigger;
    }
}
