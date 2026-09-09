package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.runicrituals.RunicRituals;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.*;

public class Sheet extends FormRune {

    @Override
    public List<Entity> getTargetEntities() {
        // need to expand aabb, because it is a 2d plane if you have minY = maxY
        BlockPos maxPos = new BlockPos(max.getX(), max.getY() + 1, max.getZ());
        BlockPos minPos = new BlockPos(min.getX(), min.getY(), min.getZ());

        AABB bb = new AABB(blockPosToVec3(minPos), blockPosToVec3(maxPos));
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
