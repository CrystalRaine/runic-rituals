package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;

public class Arcane extends ElementRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return material.getEfficiency() * cost;
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.ENCHANT,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                0.05F,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
        );
    }

    @Override
    public void updateIntensity(ActionRune action, RuneSequence sequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                sequence.intensity /= 2;
            }
            case MANIFEST -> {
                sequence.intensity += 2;
            }
            default -> {
//                do nothing
            }
        }
    }
}
