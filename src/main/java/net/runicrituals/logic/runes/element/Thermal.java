package net.runicrituals.logic.runes.element;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.references.BlockIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidIds;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;

/** <pre>
 * effects:
 *      - SACRIFICE:
 *          - freezes players / mobs
 *          - compacts ice; water -> ice -> packed ice -> blue ice
 *      - MANIFEST:
 *          - sets players / mobs on fire
 *          - decompacts ice; blue ice -> packed ice -> ice -> water
 *</pre>
 */
public class Thermal extends ElementRune{

    public Thermal() {
        super();
    }

    @Override
    public double proposeCost(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if (runningSequence.intensity >= 2 && level.getBlockState(position).is(Blocks.PACKED_ICE)) return defaultCosts(action);
                if (runningSequence.intensity >= 1 && level.getBlockState(position).is(Blocks.ICE)) return defaultCosts(action);
                if (runningSequence.intensity >= 3 && level.getBlockState(position).is(Blocks.LAVA)) return defaultCosts(action);
                if (level.getBlockState(position).is(Blocks.WATER)) return defaultCosts(action);
            }
            case MANIFEST -> {
                if (runningSequence.intensity >= 1 && level.getBlockState(position).is(Blocks.PACKED_ICE)) return defaultCosts(action);
                if (runningSequence.intensity >= 2 && level.getBlockState(position).is(Blocks.BLUE_ICE)) return defaultCosts(action);
                if (runningSequence.intensity >= 3 && level.getBlockState(position).is(Blocks.LAVA)) return defaultCosts(action);
                if (level.getBlockState(position).is(Blocks.ICE)) return defaultCosts(action);
            }
        }
        return 0;
    }

    @Override
    public void applyAction(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence){
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(runningSequence.intensity >= 2) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
                if(runningSequence.intensity >= 1) replaceBlock(level, position, Blocks.ICE, Blocks.PACKED_ICE);
                replaceBlock(level, position, Blocks.WATER, Blocks.ICE);
                if(runningSequence.intensity >= 3) replaceBlock(level, position, Blocks.LAVA, Blocks.OBSIDIAN, false);
                if(runningSequence.intensity >= 3) replaceBlock(level, position, Blocks.LAVA, Blocks.COBBLESTONE, true);
            }
            case MANIFEST -> {
                replaceBlock(level, position, Blocks.ICE, Blocks.WATER);
                if(runningSequence.intensity >= 1) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.ICE);
                if(runningSequence.intensity >= 2) replaceBlock(level, position, Blocks.BLUE_ICE, Blocks.PACKED_ICE);
                if(runningSequence.intensity >= 3) replaceBlock(level, position, ConventionalBlockTags.STONES, Blocks.LAVA);
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public double proposeCost(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if(entity instanceof ItemEntity) return 0;
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(entity.getType().fireImmune()) return 0;
                return defaultCosts(action);
            }
            case MANIFEST -> {
                if(!entity.canFreeze()) return 0;
                return defaultCosts(action);
            }
        }
        return 0;
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if(entity instanceof ItemEntity) return;
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(entity.getType().fireImmune()) return;
                if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel && entity.getTicksFrozen() < 160) {
                    entity.setTicksFrozen(entity.getTicksFrozen() + (int)(runningSequence.intensity));
                }
            }
            case MANIFEST -> {
                if(!entity.canFreeze()) return;
                if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
                    entity.setTicksFrozen(0); // no freezing while on fire, unless you set up a 140+ effective intensity ritual : )
                    if(runningSequence.intensity > 1) {
                        entity.setRemainingFireTicks((int)runningSequence.intensity * 20);
                    }
                }
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        switch (action.getActionType()) {
            case SACRIFICE -> {
                level.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                        0.05F,
                        Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
                );
            }
            case MANIFEST -> {
                level.addParticle(
                        ParticleTypes.FLAME,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                        0.05F,
                        Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
                );
            }
            default -> {
//                do nothing
            }
        }
    }
}
