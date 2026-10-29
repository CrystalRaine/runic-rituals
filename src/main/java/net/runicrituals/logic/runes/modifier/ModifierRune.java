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
    
    /**
     *
     * @param castingBlock currently executing casting block to modify
     * @return null to continue execution to the next rune. CastTrigger.KILL_EXECUTION to stop the casting block entirely. other CastTrigger.* to create a new delayed trigger
     */
    public abstract CastTrigger preformModification(CastingBlock castingBlock);
}
