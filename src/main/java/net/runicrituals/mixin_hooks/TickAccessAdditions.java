package net.runicrituals.mixin_hooks;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;

import java.util.Collection;
import java.util.Queue;

public interface TickAccessAdditions<T> {
    Collection<LevelChunkTicks<T>> runic_rituals$getAllChunkTicks();
}
