package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;

public class VoidRune extends ElementRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if(!level.isClientSide()) {
            if(entity instanceof Player player) {
                player.sendSystemMessage(Component.literal("[DEBUG] Void Effect "));
            }
        }
    }

    @Override
    public double proposeCost(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        return defaultCosts(action);
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                0.05F,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
        );
    }
}
