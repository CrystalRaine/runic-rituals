package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.*;

public class Sheet extends FormRune {

    /**
     * override for a flat "sheet" aabb
     * @return AABB for the volume
     */
    @Override
    public AABB getAABB() {
        Vec3 min = blockPosToVec3(actionLocation.offset((int) -radius, -1, (int) -radius));
        Vec3 max = blockPosToVec3(actionLocation.offset((int) radius, 1, (int) radius));
        return new AABB(min, max);
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }

    @Override
    public String name() {
        return "Sheet";
    }
}
