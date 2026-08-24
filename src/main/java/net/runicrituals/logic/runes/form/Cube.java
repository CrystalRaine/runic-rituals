package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.phys.AABB;
import net.runicrituals.RunicRituals;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Cube extends FormRune {

    int radius;
    static Random random;

    public Cube(int radius) {
        this.radius = radius;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public List<Entity> getTargetEntities(Level level, BlockPos position) {

        AABB bb = new AABB(position).inflate(radius);

        return level.getEntities(null, bb);
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public BlockPos getTargetBlock(Level level, Position position) {

//        generate a random point in the cube, but not in the 1x2x1 in the center
        int dx = random.nextInt(2*radius) - radius;
        int dy = random.nextInt(2*radius) - radius;
        int dz = random.nextInt(2*radius) - radius;

        if(dx == 0 && (dy == 0 || dy == 1 || dy == -1) && dz == 0) {
            return null;
        }

        return new BlockPos((int)position.x() + dx, (int)position.y() + dy, (int)position.z() + dz);
    }

    @Override
    public boolean isPositionInVolume(Position center, BlockPos position) {
        return Math.abs(center.x() - position.getX()) <= radius &&  Math.abs(center.y() - position.getY()) <= radius &&  Math.abs(center.z() - position.getZ()) <= radius;
    }

    @Override
    public String name() {
        return "Cube";
    }

    @Override
    public Stream<BlockPos> getAllBlocks(Level level, BlockPos center) {
        return BlockPos.betweenClosedStream(new AABB(center).inflate(radius)).filter(b -> !level.getBlockState(b).is(Blocks.AIR));
    }
}
