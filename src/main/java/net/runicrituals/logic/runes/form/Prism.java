package net.runicrituals.logic.runes.form;

/**
 * Cubic form
 * this class is extremely basic, as it is default behavior of FormRune.
 */
public class Prism extends FormRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public String name() {
        return "Prism";
    }
}
