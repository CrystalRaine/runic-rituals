package net.runicrituals.logic.runes.element;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.registries.RunicRitualsDamageTypes;

import java.util.Random;

public class Thermal extends ElementRune{

    public Thermal() {
        super();
    }

    @Override
    public void applyAction(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence){
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(runningSequence.intensity >= 2) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
                if(runningSequence.intensity >= 1) replaceBlock(level, position, Blocks.ICE, Blocks.PACKED_ICE);
                replaceBlock(level, position, Blocks.WATER, Blocks.ICE);
                if(runningSequence.intensity >= 2) replaceBlock(level, position, Blocks.LAVA, Blocks.OBSIDIAN);
            }
            case MANIFEST -> {
                replaceBlock(level, position, Blocks.ICE, Blocks.WATER);
                if(runningSequence.intensity >= 1) replaceBlock(level, position, Blocks.PACKED_ICE, Blocks.ICE);
                if(runningSequence.intensity >= 2) replaceBlock(level, position, Blocks.BLUE_ICE, Blocks.PACKED_ICE);
                if(runningSequence.intensity >= 2) replaceBlock(level, position, ConventionalBlockTags.STONES, Blocks.LAVA);
            }
            default -> {
//                do nothing
            }
        }
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        if(entity != null) {
            switch (action.getActionType()) {
                case SACRIFICE -> {
                    if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel && entity.getTicksFrozen() < 160) {
                        entity.setTicksFrozen(entity.getTicksFrozen() + (int)(runningSequence.intensity));
                    }
                }
                case MANIFEST -> {
                    if (entity instanceof LivingEntity && level instanceof ServerLevel serverLevel) {
                        entity.setTicksFrozen(0); // no freezing while on fire, unless you set up a 140+ effective intensity ritual : )
                        if(runningSequence.intensity > 1) {
                            entity.setRemainingFireTicks(runningSequence.intensity);
                        }
                    }
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

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost * invertEfficiency();
    }
}
