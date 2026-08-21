package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;

/** <pre>
 * effects:
 *      - SACRIFICE
 *          - cuts current intensity in half
 *      - MANIFEST
 *          - +1 intensity
 * </pre>
 */
public class Arcane extends ElementRune {

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
    public double proposeCost(ActionRune action, RuneSequence runningSequence) {
        return BASE_RUNE_MANA_COST / 3;
    }

    @Override
    public double updateIntensity(ActionRune action, double intensity) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                return intensity / 2;
            }
            case MANIFEST -> {
                return intensity + 1;
            }
            default -> {
//                do nothing
            }
        }
        return intensity;
    }
}
