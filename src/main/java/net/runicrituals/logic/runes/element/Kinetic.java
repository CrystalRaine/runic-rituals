package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskRuneSequence;
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
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.COPPER_FIRE_FLAME,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.83333336F,
                0.01F * Mth.randomBetween(random, -1.0F, 1.0F),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.83333336F
        );
    }
}