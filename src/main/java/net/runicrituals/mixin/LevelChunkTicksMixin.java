package net.runicrituals.mixin;

import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.mixin_hooks.LevelChunkTicksAdditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Queue;

@Mixin(LevelChunkTicks.class)
public class LevelChunkTicksMixin<T> implements LevelChunkTicksAdditions<T> {

    @Shadow
    @Final
    private Queue<ScheduledTick<T>> tickQueue;

    @Unique
    @Override
    public Queue<ScheduledTick<T>> runic_rituals$getTicksQueue() {
        return this.tickQueue;
    }
}
