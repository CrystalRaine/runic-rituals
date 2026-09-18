package net.runicrituals.logic.runes.position_modifier;

import net.minecraft.core.BlockPos;

public class Bound extends PositionModifierRune {
    @Override
    public BlockPos getOverridePosition() {

        return runeslateEntity.getPositionComponentPosition();
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return 0;
    }

    @Override
    public String name() {
        return "Bound";
    }
}
