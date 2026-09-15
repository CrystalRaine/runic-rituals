package net.runicrituals.logic.runes;

import java.util.List;

public enum RuneType {
    ELEMENT(List.of()),
    ACTION(List.of(ELEMENT)),
    FORM(List.of(ACTION)),

    FORM_MODIFIER(List.of(FORM)),
    POSITION_MODIFIER(List.of(FORM, FORM_MODIFIER)),
    CONDITION_MODIFIER(List.of(FORM, FORM_MODIFIER, POSITION_MODIFIER)),

//    not actually a rune, just used for RitualAnchorEntity in a logical role
    ANCHOR(List.of(FORM_MODIFIER, FORM));

    private final List<RuneType> argumentTypes;

    RuneType(List<RuneType> argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public boolean isValidChild(RuneType type) {
        return argumentTypes.contains(type);
    }
}
