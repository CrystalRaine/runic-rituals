package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;

public class Delay extends ModifierRune{
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        runeslateEntity.setTriggerTimeout(CastTrigger.DELAY, 10 * 20);
        return CastTrigger.DELAY;
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Delay";
    }
}
