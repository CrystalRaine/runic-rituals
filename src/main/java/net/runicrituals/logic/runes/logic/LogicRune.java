package net.runicrituals.logic.runes.logic;

import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;

public abstract class LogicRune extends Rune {
    @Override
    public RuneType getType() {
        return RuneType.LOGIC;
    }

    public void applyModification(RuneSequence sequence) {
        sequence.intensity++;
    }
}
