package net.runicrituals.logic.runes.action;

public class Manifest extends ActionRune {
    @Override
    public Action getActionType() {
        return Action.MANIFEST;
    }

    @Override
    public String name() {
        return "Manifest";
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * invertEfficiency();
    }
}
