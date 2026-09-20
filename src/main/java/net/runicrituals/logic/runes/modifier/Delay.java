package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;

public class Delay extends ModifierRune{
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {

        runeslateEntity.setTriggerTimeout(CastTrigger.DELAY_SINGLE, 10 * 20);

        return CastTrigger.DELAY_SINGLE;
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
