package net.runicrituals.logic.runes.element;

import it.unimi.dsi.fastutil.Hash;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.RunicRituals;
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

        switch (action.getActionType()) {
            case SACRIFICE -> {
                // every other, then 2/3, then 3/4, etc... -> ticks every other, 1/3, 1/4, etc...
                if((level.getGameTime() % 10) != 0) {
                    Set<BlockPos> positions = new HashSet<>();

                    form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
                        .forEach(b -> {

                            positions.add(new BlockPos(b.getX(), b.getY(), b.getZ()));

//                                BlockState bs = level.getBlockState(b);
//                                BlockEntity be = level.getBlockEntity(b);
//
//                                if(be == null) return;
//                                if(!level.getBlockState(b).hasBlockEntity()) return;
//                                if((level.getBlockEntity(b) instanceof RitualEntity)) return;
//
//                                tickBlockEntity(b, bs, be, level);
                        });

                    modifyScheduledBlockTicks(level, positions, 1);
                    modifyScheduledFluidTicks(level, positions, 1);
                }

//                RunicRituals.LOGGER.info("value: " + level.getGameTime() % 10);
            }
            case MANIFEST -> {
                Set<BlockPos> positions = new HashSet<>();

                form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
                    .forEach(b -> {

                        positions.add(new BlockPos(b.getX(), b.getY(), b.getZ()));

                        BlockState bs = level.getBlockState(b);
                        BlockEntity be = level.getBlockEntity(b);

                        if(be == null) return;
                        if(!level.getBlockState(b).hasBlockEntity()) return;
                        if((level.getBlockEntity(b) instanceof RitualEntity)) return;

                        for(int i = 0; i < runningSequence.intensity; i++) {
                            tickBlockEntity(b, bs, be, level);
                        }
                    });

                modifyScheduledBlockTicks(level, positions, (int)(-1 * runningSequence.intensity));
                modifyScheduledFluidTicks(level, positions, (int)(-1 * runningSequence.intensity));
            }
        }
    };

    @SuppressWarnings("unchecked") // like, seriously, these casts are obnoxious. i absolutely know if anyone who knows the codebase looks at this, they will go; oh! you just do xyz and suddenly they are all unnecessary
    public void modifyScheduledBlockTicks(Level level, Set<BlockPos> posList, int modification) {

        if(level.isClientSide() || !(level instanceof ServerLevel)) return;

        LevelTickAccess<Block> blockTicks = level.getBlockTicks();
        Collection<LevelChunkTicks<Block>> chunkContainers = ((TickAccessAdditions<Block>) (Object) blockTicks).runic_rituals$getAllChunkTicks();

        if (chunkContainers == null || chunkContainers.isEmpty()) return;

        List<ScheduledTick<Block>> ticksToModify = new ArrayList<>();
        long gameTime = level.getGameTime();

        for (LevelChunkTicks<Block> chunkContainer : chunkContainers) {
            Queue<ScheduledTick<Block>> queue = ((LevelChunkTicksAdditions<Block>) (Object) chunkContainer).runic_rituals$getTicksQueue();

            if (queue == null || queue.isEmpty()) continue;

            for (ScheduledTick<Block> tick : queue) {
                BlockPos tickPos = tick.pos();

                if (posList.contains(tickPos)) {
                    ticksToModify.add(tick);
                }
            }

            for(ScheduledTick<Block> tick : ticksToModify) {
                long triggerTime = tick.triggerTick();

                if(triggerTime >= (gameTime)) {
                    ((TickAccessAdditions<Block>) (Object) blockTicks).runic_rituals$removeTick(tick);
                    level.scheduleTick(tick.pos(), tick.type(), Math.max(1, (int)((triggerTime - gameTime) + modification)), tick.priority());
                }
            }

            ticksToModify.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public void modifyScheduledFluidTicks(Level level, Set<BlockPos> posList, int modification) {

        if(level.isClientSide() || !(level instanceof ServerLevel)) return;

        LevelTickAccess<Fluid> blockTicks = level.getFluidTicks();
        Collection<LevelChunkTicks<Fluid>> chunkContainers = ((TickAccessAdditions<Fluid>) (Object) blockTicks).runic_rituals$getAllChunkTicks();

        if (chunkContainers == null || chunkContainers.isEmpty()) return;

        List<ScheduledTick<Fluid>> ticksToModify = new ArrayList<>();
        long gameTime = level.getGameTime();

        for (LevelChunkTicks<Fluid> chunkContainer : chunkContainers) {
            Queue<ScheduledTick<Fluid>> queue = ((LevelChunkTicksAdditions<Fluid>) (Object) chunkContainer).runic_rituals$getTicksQueue();

            if (queue == null || queue.isEmpty()) continue;

            for (ScheduledTick<Fluid> tick : queue) {
                BlockPos tickPos = tick.pos();

                if (posList.contains(tickPos)) {
                    ticksToModify.add(tick);
                }
            }

            for(ScheduledTick<Fluid> tick : ticksToModify) {
                long triggerTime = tick.triggerTick();

                if(triggerTime > (gameTime)) {
                    ((TickAccessAdditions<Fluid>) (Object) blockTicks).runic_rituals$removeTick(tick);
                    level.scheduleTick(tick.pos(), tick.type(), Math.max(1, (int)((triggerTime - gameTime) + modification)), tick.priority());
                }
            }

            ticksToModify.clear();
        }
    }

    @SuppressWarnings("unchecked") // this unchecked is annoying, but I'm pretty sure it's necessary
    public void tickBlockEntity(BlockPos b, BlockState bs, BlockEntity be, Level level) {
        // Tick block entity
        BlockEntityTicker<?> t = bs.getTicker(level, be.getType());

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
            case MANIFEST -> entity.tick();
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
