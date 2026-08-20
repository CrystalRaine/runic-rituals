package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
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
import net.runicrituals.registries.RunicRitualsDamageTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Matter extends ElementRune {

    Random random;
    List<BlockState> settableBlocks;
    List<TagKey<Block>> intensityBlockTags = Arrays.asList(null, BlockTags.NEEDS_STONE_TOOL, BlockTags.NEEDS_IRON_TOOL, BlockTags.NEEDS_DIAMOND_TOOL);

    public Matter() {
        super();
        this.random = new Random();

        settableBlocks = new ArrayList<>();
        settableBlocks.add(Blocks.STONE.defaultBlockState());
        settableBlocks.add(Blocks.COBBLESTONE.defaultBlockState());
        settableBlocks.add(Blocks.DEEPSLATE.defaultBlockState());
        settableBlocks.add(Blocks.COBBLED_DEEPSLATE.defaultBlockState());
        settableBlocks.add(Blocks.DIORITE.defaultBlockState());
        settableBlocks.add(Blocks.GRANITE.defaultBlockState());
        settableBlocks.add(Blocks.ANDESITE.defaultBlockState());
        settableBlocks.add(Blocks.GRAVEL.defaultBlockState());
        settableBlocks.add(Blocks.DIRT.defaultBlockState());
        settableBlocks.add(Blocks.REDSTONE_ORE.defaultBlockState());
        settableBlocks.add(Blocks.IRON_ORE.defaultBlockState());
        settableBlocks.add(Blocks.COPPER_ORE.defaultBlockState());
        settableBlocks.add(Blocks.DIAMOND_ORE.defaultBlockState());
        settableBlocks.add(Blocks.GOLD_ORE.defaultBlockState());
        settableBlocks.add(Blocks.NETHERRACK.defaultBlockState());
        settableBlocks.add(Blocks.NETHER_QUARTZ_ORE.defaultBlockState());
        settableBlocks.add(Blocks.BASALT.defaultBlockState());
        settableBlocks.add(Blocks.BLACKSTONE.defaultBlockState());
        settableBlocks.add(Blocks.SAND.defaultBlockState());
        settableBlocks.add(Blocks.SANDSTONE.defaultBlockState());
        settableBlocks.add(Blocks.CALCITE.defaultBlockState());
        settableBlocks.add(Blocks.SMOOTH_BASALT.defaultBlockState());
        settableBlocks.add(Blocks.RED_SAND.defaultBlockState());
    }

    @Override
    public double proposeCost(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        return defaultCosts(action);
    }

    @Override
    public void applyAction(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence){
        switch (action.getActionType()) {
            case SACRIFICE -> {
                BlockState old = level.getBlockState(position);

                if(old.is(intensityBlockTags.get((int)runningSequence.intensity))) {
                    level.destroyBlock(position, false);
                    level.sendBlockUpdated(position, old, Blocks.AIR.defaultBlockState(), 3);
                    invertEfficiency();
                }
            }
            case MANIFEST -> {
                if(level.getBlockState(position).canBeReplaced()){
                    level.setBlockAndUpdate(position, settableBlocks.get(random.nextInt(settableBlocks.size())));
                    efficiency();
                }
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public double proposeCost(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        return defaultCosts(action);
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if(entity != null) {
            switch (action.getActionType()) {
                case SACRIFICE -> {
                    if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
                        DamageSource disintegrationDamage = new DamageSource(
                                level
                                        .registryAccess()
                                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                                        .get(RunicRitualsDamageTypes.DISINTEGRATION_DAMAGE)
                                        .orElseThrow()
                        );
                        entity.hurtServer(serverLevel, disintegrationDamage, (int)runningSequence.intensity);
                        efficiency();
                    }
                }
                case MANIFEST -> {

                }
                default -> {
//                do nothing
                }
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
}
