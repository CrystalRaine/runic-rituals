package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskRuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;

public class VoidRune extends ElementRune {

    @Override
    public double proposeCostForIntensityChange(ActionRune action, CastingBlock block) {
        return defaultCosts(action) * 10;
    }


    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.END_ROD,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                0.05F,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
        );
    }
}
