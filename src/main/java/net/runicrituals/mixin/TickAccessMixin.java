package net.runicrituals.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.mixin_hooks.TickAccessAdditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

@Mixin(LevelTicks.class)
public class TickAccessMixin<T> implements TickAccessAdditions<T> {

    @Shadow
    @Final
    private Long2ObjectMap<LevelChunkTicks<T>> allContainers;


    @Override
    public Collection<LevelChunkTicks<T>> runic_rituals$getAllChunkTicks() {
        return allContainers.values();
    }
}
