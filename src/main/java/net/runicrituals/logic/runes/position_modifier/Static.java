package net.runicrituals.logic.runes.position_modifier;

import net.minecraft.core.BlockPos;

public class Static extends PositionModifierRune {

    @Override
    public BlockPos getOverridePosition() {
        return null;
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Static";
    }
}
