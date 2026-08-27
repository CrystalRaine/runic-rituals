package net.runicrituals.mixin;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import net.runicrituals.mixin_hooks.LevelChunkTicksAdditions;
import net.runicrituals.mixin_hooks.TickAccessAdditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

@Mixin(LevelTicks.class)
public class TickAccessMixin<T> implements TickAccessAdditions<T> {

    @Shadow
    @Final
    private Long2ObjectMap<LevelChunkTicks<T>> allContainers;

    @Shadow @Final
    private Long2LongMap nextTickForContainer;

    @Unique
    @Override
    public Collection<LevelChunkTicks<T>> runic_rituals$getAllChunkTicks() {
        return allContainers.values();
    }

    @Shadow
    public void clearArea(final BoundingBox area){}

    @Unique
    @Override
    public void runic_rituals$removeTick(ScheduledTick<T> tick) {
        clearArea(new BoundingBox(tick.pos().getX(), tick.pos().getY(), tick.pos().getZ(), tick.pos().getX(), tick.pos().getY(), tick.pos().getZ()));
    }
}
