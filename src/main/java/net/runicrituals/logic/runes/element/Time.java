package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.mixin_hooks.EntityAdditions;
import net.runicrituals.registries.blocks.RitualEntity;

public class Time extends ElementRune {

    public Time() {
        super(true);
    }

    @Override
    public void applyAction(Level level, FormRune form, Position ritualCenter, ActionRune action, RuneSequence runningSequence) {
        form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
            .filter(b -> level.getBlockState(b).hasBlockEntity())
            .filter(b -> !(level.getBlockEntity(b) instanceof RitualEntity))
            .forEach(b -> {
                BlockState bs = level.getBlockState(b);
                BlockEntity be = level.getBlockEntity(b);
                if(be == null) return;
                BlockEntityTicker<?> t = bs.getTicker(level, be.getType());

                @SuppressWarnings("unchecked")
                BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) t;
                if(ticker == null) return;
                ticker.tick(level, b, bs, be);
            });
    };

    @Override
    public double proposeCostForBlock(Level level, FormRune form, Position initialPos, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        return defaultCosts(action) * 2;
    }

    @Override
    public void applyAction(Level level, FormRune form, Position ritualCenter, BlockPos actAt, ActionRune action, RuneSequence runningSequence) {
//        accelerate/decelerate random ticks
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(!level.getBlockState(actAt).is(Blocks.AIR)) {

                }
            }
            case MANIFEST -> {
                if(!level.getBlockState(actAt).is(Blocks.AIR)) {
                    level.getBlockState(actAt).randomTick((ServerLevel) level, actAt, level.getRandom());
                    level.getBlockState(actAt).randomTick((ServerLevel) level, actAt, level.getRandom());
                }
            }
            default -> {}
        }
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        return defaultCosts(action) * 2;
    }

    @Override
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
//        accelerate/decelerate motion
//        scaleEntityMotion((EntityAdditions) entity, action, runningSequence);

        switch (action.getActionType()) {
            case SACRIFICE -> {
            }
            case MANIFEST -> {
                entity.tick();
            }
            default -> {}
        }
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.DUST_PLUME,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                0.05F,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
        );
    }

}
