package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.*;

public class Sheet extends FormRune {

    @Override
    public List<Entity> getTargetEntities() {

        AABB bb = new AABB(blockPosToVec3(min), blockPosToVec3(max));
        List<Entity> entities = level.getEntities(null, bb);
        return entities.stream().filter(e -> base.contains(getBlockPosition(e.position()))).toList();
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public BlockPos getTargetBlock() {
        return getRandom(level, base);
    }

    @Override
    public boolean isPositionInVolume(BlockPos pos) {
        return base.contains(pos);
    }

    @Override
    public String name() {
        return "Sheet";
    }

    @Override
    public Stream<BlockPos> getAllBlocks() {
        return base.stream();
    }
}
