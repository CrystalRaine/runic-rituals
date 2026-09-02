package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.mixin_hooks.*;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity;

import java.util.*;

/**
 * aside from (maybe) the space rune, this is probably going to be the most complex rune available.
 * primarily, it does two things;
 * manifest: increases the ticks all entities, blocks etc. get in its radius
 * sacrifice: reduces the ticks all entities, blocks, etc. get in radius
 *
 * note that this may cause odd redstone behavior, but *does* work with redstone
 * otherwise, fluids flow quicker, mobs/players move faster, furnaces and other block entities speed up operations, crops grow quicker, etc.
 * also. this was a pain to implement lol - took me like a whole week
 *
 * also-also, there's a bunch of mixins that implement this behavior on the other side of things, since (particularly Time-Sacrifice) effects can't be done as a one-time-effect
 */
public class Time extends ElementRune {

    public Time() {
        super(true);
    }

    @Override
    public void applyAction(Level level, FormRune form, Position ritualCenter, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                Set<BlockPos> positions = new HashSet<>();

                form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
                    .forEach(b -> {
                        if(level.getGameTime() % (runningSequence.intensity + 1) != 0) {
                            positions.add(new BlockPos(b.getX(), b.getY(), b.getZ()));

                            BlockEntity be = level.getBlockEntity(b);
                            if (be != null && !(be instanceof RuneObeliskEntity)) {
                                ((BlockEntityAdditions) be).runic_rituals$setExtraTicks(-1);
                            }
                        }

                        LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(b.getX()), SectionPos.blockToSectionCoord(b.getZ()));
                        ((LevelChunkAdditions)chunk).runic_rituals$setRandomTickDelay(b, 10000);
                    }
                );

                if(level.getGameTime() % (runningSequence.intensity + 1) != 0) {

                    modifyScheduledBlockTicks(level, positions, 1);
                    modifyScheduledFluidTicks(level, positions, 1);
                }
            }
            case MANIFEST -> {
                Set<BlockPos> positions = new HashSet<>();

                form.getAllBlocks(level, new BlockPos((int)ritualCenter.x(), (int)ritualCenter.y(), (int)ritualCenter.z()))
                    .forEach(b -> {
                        positions.add(new BlockPos(b.getX(), b.getY(), b.getZ()));

                        BlockEntity be = level.getBlockEntity(b);
                        if(be != null && !(be instanceof RuneObeliskEntity)) {
                            ((BlockEntityAdditions) be).runic_rituals$setExtraTicks((int)runningSequence.intensity);
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

    @Override
    public double proposeCostForBlock(Level level, FormRune form, Position initialPos, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                return -BASE_RUNE_MANA_COST * invertEfficiency();
            }
            case MANIFEST -> {
                return BASE_RUNE_MANA_COST * efficiency();
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                return -BASE_RUNE_MANA_COST * invertEfficiency();
            }
            case MANIFEST -> {
                return BASE_RUNE_MANA_COST * efficiency();
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void applyAction(Level level, FormRune form, Position ritualCenter, BlockPos actAt, ActionRune action, RuneSequence runningSequence) {
//        accelerate/decelerate random ticks
        switch (action.getActionType()) {
            case SACRIFICE -> {
//                this is operated slightly differently (up in ApplyAction for all blocks in range)
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
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                if(level.getGameTime() % (runningSequence.intensity + 1) != 0) {
                    ((EntityAdditions)entity).runic_rituals$suppressNextTick();
                }
                if(entity instanceof Player) {
                    scaleEntityMotion((EntityAdditions)entity, action, runningSequence);
                }
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
