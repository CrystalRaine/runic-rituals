package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;

public class Static extends ModifierRune {

    @Override
    public RuneType getType() {
        return RuneType.MODIFIER;
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Static";
    }

    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        // TODO: this is literally a do-nothing position rune. may need to edit this later to get intended behavior
        // given this is intended to turn a one-time trigger into a continuous location effect.

        return null;
    }
}
