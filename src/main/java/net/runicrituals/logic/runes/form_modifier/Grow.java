package net.runicrituals.logic.runes.form_modifier;

import net.runicrituals.logic.runes.form.FormRune;

public class Grow extends FormModifierRune {
    @Override
    public void applyModificationToForm(FormRune rune) {
        rune.setRadius(rune.getRadius() * 1.25f);
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
//        yeah, this isn't technically scaled properly, should scale with 1.5^3 or something, but have a discount : )
        return cost * 1.05f;
    }

    @Override
    public String name() {
        return "Grow";
    }
}
