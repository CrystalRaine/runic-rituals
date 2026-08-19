package net.runicrituals.logic.runes.action;

import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;

public abstract class ActionRune extends Rune {

    public enum Action {
        SACRIFICE,
        MANIFEST,
    }

    @Override
    public RuneType getType() {
        return RuneType.ACTION;
    }

    public abstract Action getActionType();
}
