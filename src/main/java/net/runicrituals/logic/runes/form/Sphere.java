package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneInlayMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sphere extends FormRune{

    int radius;
    static Random random;

    public Sphere(int radius) {
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

            entities = entities.stream().filter(e -> e.distanceToSqr(new Vec3((int)position.x(), (int)position.y(), (int)position.z())) < radius * radius).toList();
            return entities;
        }

        return new ArrayList<>();
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public BlockPos getTargetBlock(Level level, Position position) {

        double x = random.nextGaussian();
        double y = random.nextGaussian();
        double z = random.nextGaussian();

        Vec3 direction = new Vec3(x,y,z).normalize();
        double distance = Math.pow(random.nextDouble(), 1/3f);

        direction = direction.scale(distance).scale(radius);

        int dx = (int)direction.x();
        int dy = (int)direction.y();
        int dz = (int)direction.z();

        if(dx == 0 && (dy == 0 || dy == 1 || dy == -1) && dz == 0) {
            return null;
        }

        return new BlockPos((int)(position.x() + dx), (int)(position.y() + dy), (int)(position.z() + dz));
    }

    @Override
    public String name() {
        return "Sphere";
    }
}
