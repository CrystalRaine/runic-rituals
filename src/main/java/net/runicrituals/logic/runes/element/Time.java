package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.mixin_hooks.LevelChunkTicksAdditions;
import net.runicrituals.mixin_hooks.TickAccessAdditions;
import net.runicrituals.registries.blocks.RitualEntity;

import java.util.*;

public class Time extends ElementRune {

    public Time() {
        super(true);
    }

    @Override
    public void applyAction(Level level, FormRune form, Position ritualCenter, ActionRune action, RuneSequence runningSequence) {
        List<BlockPos> posList = form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
            .filter(b -> level.getBlockState(b).hasBlockEntity())
            .filter(b -> !(level.getBlockEntity(b) instanceof RitualEntity))
            .toList();

        posList
            .forEach(b -> {
                BlockState bs = level.getBlockState(b);
                BlockEntity be = level.getBlockEntity(b);

                if(be == null) return;
                tickBlockEntity(b, bs, be, level);
            });

        // i wanted this to be in the above for-each, but it's easier (and probably faster) to compile-then-compare
        shortenScheduledTicks(level, posList);

    };

    public void shortenScheduledTicks(Level level, List<BlockPos> posList) {

        if(level.isClientSide() || !(level instanceof ServerLevel)) return;

        LevelTickAccess<Block> blockTicks = level.getBlockTicks();
        @SuppressWarnings("unchecked") // sigh, these are the worst part of this rune's implementation
        Collection<LevelChunkTicks<Block>> chunkContainers = ((TickAccessAdditions<Block>) (Object) blockTicks).runic_rituals$getAllChunkTicks();

        if (chunkContainers == null || chunkContainers.isEmpty()) return;

        List<ScheduledTick<Block>> ticksToModify = new ArrayList<>();
        long gameTime = level.getGameTime();

        for (LevelChunkTicks<Block> chunkContainer : chunkContainers) {
            @SuppressWarnings("unchecked") // like, seriously, these casts are obnoxious. i absolutely know if anyone who knows the codebase looks at this, they will go; oh! you just do xyz and suddenly they are all unnecessary
            Queue<ScheduledTick<Block>> queue = ((LevelChunkTicksAdditions<Block>) (Object) chunkContainer).runic_rituals$getTicksQueue();

            if (queue == null || queue.isEmpty()) continue;

            Iterator<ScheduledTick<Block>> queueIterator = queue.iterator();

            while(queueIterator.hasNext()) {
                ScheduledTick<Block> tick = queueIterator.next();
                BlockPos tickPos = tick.pos();

                if(posList.contains(tickPos)) {
                    ticksToModify.add(tick);
                    queueIterator.remove();
                }
            }

            for(ScheduledTick<Block> tick : ticksToModify) {
                long triggerTime = tick.triggerTick();

                if(triggerTime > gameTime) {
                    long newTriggerTime = triggerTime - 1;
                    chunkContainer.schedule(new ScheduledTick<>(tick.type(), tick.pos(), newTriggerTime, tick.subTickOrder()));
                }

                queue.add(tick);
            }

            ticksToModify.clear();
        }
    }

    public void tickBlockEntity(BlockPos b, BlockState bs, BlockEntity be, Level level) {
        // Tick block entity
        BlockEntityTicker<?> t = bs.getTicker(level, be.getType());

        // this unchecked is annoying, but I'm pretty sure it's necessary.
        @SuppressWarnings("unchecked")
        BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>) t;
        if(ticker == null) return;
        ticker.tick(level, b, bs, be);
    }

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
