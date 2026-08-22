package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;

import java.util.List;

public abstract class FormRune extends Rune {
    @Override
    public RuneType getType() {
        return RuneType.FORM;
    }

    public abstract List<Entity> getTargetEntities(Level level, Position position);
    public abstract BlockPos getTargetBlock(Level level, Position position);

    public abstract boolean isPositionInVolume(Position center, BlockPos position);

    public abstract String name();
}
