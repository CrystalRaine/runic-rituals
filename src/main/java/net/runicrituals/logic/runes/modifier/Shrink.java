package net.runicrituals.logic.runes.modifier;

import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.logic.runes.form.FormRune;

public class Shrink extends ModifierRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
//        yeah, this isn't technically scaled properly, no discount here : (
        return cost * 0.95f;
    }

    @Override
    public String name() {
        return "Shrink";
    }

    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        FormRune form = castingBlock.getForm();
        form.setRadius(form.getRadius() * 0.75f);

        return null;
    }
}
