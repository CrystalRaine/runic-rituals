package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.blockPosToVec3;

public class Dome extends FormRune {

    @Override
    public List<Entity> getTargetEntities() {
        return level.getEntities(null, getAABB()).stream().filter(e -> e.position().distanceToSqr(blockPosToVec3(actionLocation).add(0.5,1,0.5)) < ((radius + 0.5) * (radius + 0.5))).toList();
    }

    @Override
    public Stream<BlockPos> getAllBlocks() {
        return super.getAllBlocks().filter(b -> b.distToCenterSqr(blockPosToVec3(actionLocation).add(0.5,0.5,0.5)) < ((radius + 0.5) * (radius + 0.5)));
    }

    @Override
    public AABB getAABB() {
        Vec3 min = blockPosToVec3(actionLocation.offset((int) -radius, 0, (int) -radius));
        Vec3 max = blockPosToVec3(actionLocation.offset((int) radius, (int) radius, (int) radius));
        return new AABB(min, max);
    }

    @Override
    public String name() {
        return "Dome";
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * efficiency();
    }
}
