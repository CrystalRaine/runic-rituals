package net.runicrituals.mixin_hooks;

import net.minecraft.world.ticks.ScheduledTick;

import java.util.Queue;

public interface LevelChunkTicksAdditions<T> {

    default Queue<ScheduledTick<T>> runic_rituals$getTicksQueue(){throw new IllegalStateException("Implemented via Mixin");};
}
