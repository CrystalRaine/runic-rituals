package net.runicrituals.logic.runes.condition_modifier;

import net.runicrituals.logic.runes.enums.CastTriggers;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.enums.RuneType;

public abstract class ConditionModifierRune extends Rune {
    @Override
    public RuneType getType() {
        return RuneType.CONDITION_MODIFIER;
    }

    public abstract boolean passesCheck();

    public abstract CastTriggers getCastTrigger();
}
