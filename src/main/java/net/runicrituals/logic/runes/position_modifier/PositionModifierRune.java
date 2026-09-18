package net.runicrituals.logic.runes.position_modifier;

import net.minecraft.core.BlockPos;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.enums.RuneType;

public abstract class PositionModifierRune extends Rune {

    public abstract BlockPos getOverridePosition();

    @Override
    public RuneType getType() {
        return RuneType.POSITION_MODIFIER;
    }
}
