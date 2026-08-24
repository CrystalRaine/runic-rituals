package net.runicrituals.logic.runes.element;

import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.registries.RunicRitualsDamageTypes;

import java.util.*;

/**<pre>
 * effects:
 *      - SACRIFICE
 *          - deletes blocks from existence based on intensity
 *          - damages players
 *      - MANIFEST
 *          - creates blocks based on intensity
 * </pre>
 */
public class Matter extends ElementRune {

    List<BlockState> settableBlocks;
    List<TagKey<Block>> intensityBlockTags = Arrays.asList(BlockTags.INCORRECT_FOR_WOODEN_TOOL, BlockTags.INCORRECT_FOR_STONE_TOOL, BlockTags.INCORRECT_FOR_COPPER_TOOL, BlockTags.INCORRECT_FOR_IRON_TOOL, BlockTags.INCORRECT_FOR_GOLD_TOOL, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, BlockTags.INCORRECT_FOR_NETHERITE_TOOL);

    public Matter() {
        super();

        settableBlocks = new ArrayList<>();
    }

    @Override
    public double proposeCostForBlock(Level level, FormRune form, Position initialPos, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(canDestroyBlock(level, position, runningSequence.intensity)) {
                    return BASE_RUNE_MANA_COST * invertEfficiency() * 5;
                }
                return 0;
            }
            case MANIFEST -> {
                if(level.getBlockState(position).canBeReplaced()){
                    return BASE_RUNE_MANA_COST * efficiency() * 5;
                } else {
                    return 0;
                }
            }
            default -> {
//                do nothing
            }
        }
        return 0;
    }

    @Override
    public void applyAction(Level level, FormRune form, Position initialPos, BlockPos position, ActionRune action, RuneSequence runningSequence){
        switch (action.getActionType()) {
            case SACRIFICE -> {
                BlockState old = level.getBlockState(position);

                if(canDestroyBlock(level, position, runningSequence.intensity)) {
                    level.destroyBlock(position, false);
                    level.sendBlockUpdated(position, old, Blocks.AIR.defaultBlockState(), 3);
                }
            }
            case MANIFEST -> {
                if(level.getBlockState(position).canBeReplaced()){

                    List<TagKey<Block>> tagSet = List.of(BlockTags.DIRT, BlockTags.DIRT, BlockTags.BASE_STONE_OVERWORLD, BlockTags.BASE_STONE_OVERWORLD, BlockTags.BASE_STONE_NETHER, BlockTags.BASE_STONE_NETHER, BlockTags.COPPER_ORES, BlockTags.COPPER_ORES, BlockTags.IRON_ORES, BlockTags.IRON_ORES, BlockTags.GOLD_ORES);
                    List<Block> aggregateOptions = new ArrayList<>();
                    HolderGetter<Block> lookup = level.registryAccess().lookupOrThrow(Registries.BLOCK);

                    for (int i = 0; i < Math.min(runningSequence.intensity, tagSet.size()); i++) {
                        aggregateOptions.addAll(lookup.get(tagSet.get(i)).stream().flatMap(holderSet -> holderSet.stream().map(Holder::value)).toList());
                    }

                    if(runningSequence.intensity >= tagSet.size()) {
                        aggregateOptions.add(Blocks.DEEPSLATE_DIAMOND_ORE);
                    }

                    level.setBlockAndUpdate(position, aggregateOptions.get(level.getRandom().nextInt(aggregateOptions.size())).defaultBlockState());
                }
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if (Objects.requireNonNull(action.getActionType()) == ActionRune.Action.SACRIFICE) {
            if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
                return defaultCosts(action) * 3;
            }
        }
        return 0;
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if (Objects.requireNonNull(action.getActionType()) == ActionRune.Action.SACRIFICE) {
            if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
                DamageSource disintegrationDamage = new DamageSource(
                        level
                                .registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(RunicRitualsDamageTypes.DISINTEGRATION_DAMAGE)
                                .orElseThrow()
                );
                entity.hurtServer(serverLevel, disintegrationDamage, (int) runningSequence.intensity);
            }
        }
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        switch (action.getActionType()) {
            case SACRIFICE -> {
                level.addParticle(
                        ParticleTypes.ASH,
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
                        ParticleTypes.POOF,
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

    private boolean canDestroyBlock(Level level, BlockPos pos, double intensity) {
        BlockState block = level.getBlockState(pos);

        if(level.getBlockState(pos).is(Blocks.AIR)) {
            return false;
        }

        for(int tryIndex = 0; tryIndex <= Math.min(intensity, intensityBlockTags.size()); tryIndex++) {
            if(!block.is(intensityBlockTags.get(tryIndex))) {
                return true;
            }
        }

        return false;
    }
}
