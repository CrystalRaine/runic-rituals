package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;

public abstract class ModifierRune extends Rune {

    @Override
    public RuneType getType() {
        return RuneType.MODIFIER;
    }

    public abstract CastTrigger preformModification(CastingBlock castingBlock);
}
