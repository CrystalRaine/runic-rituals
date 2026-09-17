package net.runicrituals.logic.runes.form_modifier;

import net.runicrituals.logic.runes.form.FormRune;

public class Shrink extends ModifierRune {
    @Override
    public void applyModificationToForm(FormRune rune) {
        rune.setRadius(rune.getRadius() * 0.75f);
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
//        yeah, this isn't technically scaled properly, no discount here : (
        return cost * 0.95f;
    }

    @Override
    public String name() {
        return "Shrink";
    }
}
