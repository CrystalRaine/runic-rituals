package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;

public class Control extends ModifierRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Control";
    }

    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        return CastTrigger.USE_BOUND_ITEM;
    }
}
