package net.runicrituals.logic.runes.logic;

import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;

public class Grow extends ModifierRune {
    @Override
    public void applyModificationToElement(ElementRune rune) {

    }

    @Override
    public void applyModificationToAction(ActionRune rune) {

    }

    @Override
    public void applyModificationToForm(FormRune rune) {
//        TODO: Reimplement
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
//        yeah, this isn't technically scaled properly, should scale with 1.5^2, but have a discount : )
        return cost * 1.5;
    }
}
