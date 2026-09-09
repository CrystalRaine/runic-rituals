package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.mixin_hooks.EntityAdditions;

public class Kinetic extends ElementRune {

    public Kinetic() {
        super(true);
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
        return defaultCosts(action);
    }

    @Override
    public void applyActionOnEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {

        scaleEntityMotion((EntityAdditions) entity, action, block);
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        createParticle(level, pos, ParticleTypes.COPPER_FIRE_FLAME, new Vec3(0.9, 0.01, 0.9));
    }
}