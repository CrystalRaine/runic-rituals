package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.runicrituals.logic.RuneInlayMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cube extends FormRune {

    int radius;
    static Random random;

    public Cube(int radius) {
        this.radius = radius;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public List<Entity> getTargetEntities(Level level, Position position) {
        if (!level.isClientSide()) {
            List<Entity> entities = new ArrayList<>();

            BlockPos blockPos = new BlockPos((int)position.x(), (int)position.y(), (int)position.z());

            AABB bb = new AABB(blockPos).inflate(radius);
            entities.addAll(level.getEntities(null, bb));

            return entities;
        }

        return new ArrayList<>();
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
    public String name() {
        return "Cube";
    }
}
