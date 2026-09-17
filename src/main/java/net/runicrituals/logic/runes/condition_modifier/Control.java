package net.runicrituals.logic.runes.condition_modifier;

import net.runicrituals.logic.runes.enums.CastTriggers;

public class Control extends ConditionModifierRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return 0;
    }

    @Override
    public CastTriggers getCastTrigger() {
        return CastTriggers.USE_BOUND_ITEM;
    }

    @Override
    public String name() {
        return "Control";
    }
}
