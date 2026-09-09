package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
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
        createParticle(level, pos, ParticleTypes.ENCHANT, new Vec3(0.09, 0.05, 0.09));
    }

    @Override
    public double proposeCostForIntensityChange(ActionRune action, CastingBlock block) {
        return defaultCosts(action) / 3;
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
