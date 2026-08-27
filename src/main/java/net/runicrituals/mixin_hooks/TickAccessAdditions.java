package net.runicrituals.mixin_hooks;

import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;

import java.util.Collection;

public interface TickAccessAdditions<T> {
    Collection<LevelChunkTicks<T>> runic_rituals$getAllChunkTicks();

    void runic_rituals$removeTick(ScheduledTick<T> tick);
}
