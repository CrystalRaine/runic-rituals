package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.*;

public class Prism extends FormRune {

    @Override
    public List<Entity> getTargetEntities() {
        int rad = Math.max(max.getX() - min.getX(), max.getZ() - min.getZ()) / 2;
        BlockPos maxPlusRad = new BlockPos(max.getX(), max.getY() + rad, max.getZ());
        BlockPos minMinusRad = new BlockPos(min.getX(), min.getY() - rad, min.getZ());

        AABB bb = new AABB(blockPosToVec3(minMinusRad), blockPosToVec3(maxPlusRad));
        List<Entity> entities = level.getEntities(null, bb);

        return entities.stream().filter(e -> {
            BlockPos p = getBlockPosition(e.position());
            BlockPos adjust = new BlockPos(p.getX(), min.getY(), p.getZ());
            return base.contains(adjust);
        }).toList();
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public BlockPos getTargetBlock() {
        BlockPos pos = getRandom(level, base).immutable();
        int rad = Math.max(max.getX() - min.getX(), max.getZ() - min.getZ());
        rad = level.getRandom().nextInt(rad) - (rad / 2);

        return new BlockPos(pos.getX(), pos.getY() + rad, pos.getZ());
    }

    @Override
    public boolean isPositionInVolume(BlockPos pos) {
        int rad = Math.max(max.getX() - min.getX(), max.getZ() - min.getZ()) / 2;
        BlockPos maxPlusRad = new BlockPos(max.getX(), max.getY() + rad, max.getZ());
        BlockPos minMinusRad = new BlockPos(min.getX(), min.getY() - rad, min.getZ());

        if(pos.getX() > maxPlusRad.getX() || pos.getY() > maxPlusRad.getY() || pos.getZ() > maxPlusRad.getZ()) return false;
        if(pos.getX() < minMinusRad.getX() || pos.getY() < minMinusRad.getY() || pos.getZ() < minMinusRad.getZ()) return false;

        BlockPos adjust = new BlockPos(pos.getX(), min.getY(), pos.getZ());
        return base.contains(adjust);
    }

    @Override
    public String name() {
        return "Prism";
    }

    @Override
    public Stream<BlockPos> getAllBlocks() {
        int rad = Math.max(max.getX() - min.getX(), max.getZ() - min.getZ()) / 2;
        BlockPos maxPlusRad = new BlockPos(max.getX(), max.getY() + rad, max.getZ());
        BlockPos minMinusRad = new BlockPos(min.getX(), min.getY() - rad, min.getZ());

        return BlockPos.betweenClosedStream(maxPlusRad, minMinusRad).filter(p -> base.contains(new BlockPos(p.getX(), min.getY(), p.getZ())));
    }
}
