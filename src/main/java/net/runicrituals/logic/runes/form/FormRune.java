package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;

import java.util.List;
import java.util.stream.Stream;

public abstract class FormRune extends Rune {

    Level level;
    List<BlockPos> base;
    BlockPos min;
    BlockPos max;

    public void setProperties(Level level, List<BlockPos> base, BlockPos min, BlockPos max) {
        this.level = level;
        this.base = base;
        this.min = min;
        this.max = max;
    }

    @Override
    public RuneType getType() {
        return RuneType.FORM;
    }

    public abstract List<Entity> getTargetEntities();
    public abstract BlockPos getTargetBlock();
    public abstract boolean isPositionInVolume(BlockPos pos);
    public abstract String name();
    public abstract Stream<BlockPos> getAllBlocks();
}
