package net.runicrituals.logic.runes.enums;

import java.util.List;

public enum RuneType {
    ELEMENT(List.of(), false, 0),
    ACTION(List.of(ELEMENT), false, 8),
    FORM(List.of(ACTION), false, 8),

    MODIFIER(List.of(FORM), true, 1),

    MANA_BOUND(List.of(FORM, MODIFIER), false, 1),

//    not actually a rune, just used for RitualAnchorEntity in a logical role
    ANCHOR(List.of(MODIFIER, FORM, MANA_BOUND), false, 8);

    private final List<RuneType> argumentTypes;
    private final boolean selfAsValidChild;
    private final int childCountMax;

    RuneType(List<RuneType> argumentTypes, boolean selfAsValidChild, int childCountMax) {
        this.selfAsValidChild = selfAsValidChild;
        this.argumentTypes = argumentTypes;
        this.childCountMax = childCountMax;
    }

    public boolean isValidChild(RuneType type) {
        return argumentTypes.contains(type) || (type == this && selfAsValidChild);
    }

    public int getChildCountMax() {
        return childCountMax;
    }
}
