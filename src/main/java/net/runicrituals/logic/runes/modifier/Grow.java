package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.logic.runes.form.FormRune;

public class Grow extends ModifierRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
//        yeah, this isn't technically scaled properly, should scale with 1.5^3 or something, but have a discount : )
        return cost * 1.05f;
    }

    @Override
    public String name() {
        return "Grow";
    }

    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        FormRune form = castingBlock.getForm();
        form.setRadius(form.getRadius() * 1.25f);

        return null;
    }
}
