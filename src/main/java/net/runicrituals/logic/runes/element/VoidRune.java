package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.action.ActionRune;

public class VoidRune extends ElementRune {

    @Override
    public double proposeCostForIntensityChange(ActionRune action, CastingBlock block) {
        return defaultCosts(action) * 10;
    }


    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        createParticle(level, pos, ParticleTypes.END_ROD, new Vec3(0.09, 0.05, 0.09));
    }
}
