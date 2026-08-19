package net.runicrituals.logic.runes.action;

public class Sacrifice extends ActionRune {
    @Override
    public Action getActionType() {
        return Action.SACRIFICE;
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return -cost * material.getEfficiency();
    }
}
