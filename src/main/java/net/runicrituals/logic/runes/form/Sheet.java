package net.runicrituals.logic.runes.form;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import static net.runicrituals.logic.Util.*;

public class Sheet extends FormRune {

    /**
     * override for a flat "sheet" aabb
     * @return AABB for the volume
     */
    @Override
    public AABB getAABB() {
        Vec3 min = actionLocation.add((int) -radius, -1, (int) -radius);
        Vec3 max = actionLocation.add((int) radius, -1, (int) radius);
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
