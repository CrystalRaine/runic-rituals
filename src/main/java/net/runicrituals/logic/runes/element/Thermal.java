package net.runicrituals.logic.runes.element;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;

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
    public double proposeCostForBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if (block.intensity >= 2 && level.getBlockState(position).is(Blocks.PACKED_ICE)) return defaultCosts(action);
                if (block.intensity >= 1 && level.getBlockState(position).is(Blocks.ICE)) return defaultCosts(action);
                if (block.intensity >= 3 && level.getBlockState(position).is(Blocks.LAVA)) return defaultCosts(action);
                if (level.getBlockState(position).is(Blocks.WATER)) return defaultCosts(action);
            }
            case MANIFEST -> {
                if (block.intensity >= 1 && level.getBlockState(position).is(Blocks.PACKED_ICE)) return defaultCosts(action);
                if (block.intensity >= 2 && level.getBlockState(position).is(Blocks.BLUE_ICE)) return defaultCosts(action);
                if (block.intensity >= 3 && level.getBlockState(position).is(Blocks.LAVA)) return defaultCosts(action);
                if (level.getBlockState(position).is(Blocks.ICE)) return defaultCosts(action);
            }
        }
        return 0;
    }

    @Override
    public void applyActionOnBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block){
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(block.intensity >= 2) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
                if(block.intensity >= 1) replaceBlock(level, position, Blocks.ICE, Blocks.PACKED_ICE);
                replaceBlock(level, position, Blocks.WATER, Blocks.ICE);
                if(block.intensity >= 3) replaceBlock(level, position, Blocks.LAVA, Blocks.OBSIDIAN, false);
                if(block.intensity >= 3) replaceBlock(level, position, Blocks.LAVA, Blocks.COBBLESTONE, true);
            }
            case MANIFEST -> {
                if(level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, position) && replaceBlock(level, position, Blocks.ICE, Blocks.AIR)) {
                    RandomSource random = level.getRandom();
                    for (int i = 0; i < 8; i++) {
                        level.addParticle(ParticleTypes.LARGE_SMOKE, position.getX() + random.nextFloat(), position.getY() + random.nextFloat(), position.getZ() + random.nextFloat(), 0.0, 0.0, 0.0);
                    }
                } else {
                    replaceBlock(level, position, Blocks.ICE, Blocks.WATER);
                }
                if(block.intensity >= 1) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.ICE);
                if(block.intensity >= 2) replaceBlock(level, position, Blocks.BLUE_ICE, Blocks.PACKED_ICE);
                if(block.intensity >= 3) replaceBlock(level, position, ConventionalBlockTags.STONES, Blocks.LAVA);
                if(block.intensity >= 5) replaceBlock(level, position, Blocks.OBSIDIAN, Blocks.LAVA);
                if(block.intensity >= 6) replaceBlock(level, position, Blocks.CRYING_OBSIDIAN, Blocks.LAVA);
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
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
    public void applyActionOnEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
        if(entity instanceof ItemEntity) return;
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(entity.getType().fireImmune()) return;
                if (entity instanceof LivingEntity && !level.isClientSide() && entity.getTicksFrozen() < 160) {
                    entity.setTicksFrozen(entity.getTicksFrozen() + (int)(block.intensity));
                }
            }
            case MANIFEST -> {
                if(!entity.canFreeze()) return;
                if (entity instanceof LivingEntity && !level.isClientSide()) {
                    entity.setTicksFrozen(0); // no freezing while on fire, unless you set up a 140+ effective intensity ritual : )
                    if(block.intensity > 1) {
                        entity.setRemainingFireTicks((int)block.intensity * 20);
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
                createParticle(level, pos, ParticleTypes.SNOWFLAKE, new Vec3(0.09, 0.05, 0.09));
            }
            case MANIFEST -> {
                createParticle(level, pos, ParticleTypes.DUST_PLUME, new Vec3(0.09, 0.05, 0.09));
            }
            default -> {
//                do nothing
            }
        }
    }
}
