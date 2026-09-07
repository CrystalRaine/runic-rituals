package net.runicrituals.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Util {

    public static Vec3 blockPosToVec3(BlockPos bp) {
        return new Vec3(bp.getX(), bp.getY(), bp.getZ());
    }

    public static BlockPos getBlockPosition(Position position) {
        return new BlockPos((int)position.x(), (int)position.y(), (int)position.z());
    }

    public static <T> T getRandom(Level level, List<T> list) {
        int index = level.getRandom().nextInt(list.size());
        return list.get(index);
    }




}
