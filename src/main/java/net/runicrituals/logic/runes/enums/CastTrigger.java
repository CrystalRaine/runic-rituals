package net.runicrituals.logic.runes.enums;

public enum CastTrigger {

    USE_BOUND_ITEM(false, true),
    ENTITY_HIT_WITH_BOUND_ITEM(true, true),
    DELAY_CONTINUOUS(false, true),
    DELAY_SINGLE(true, true),
    REDSTONE_POWERED(false, false)

    ;

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
