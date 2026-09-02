package net.runicrituals.mixin;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.runicrituals.mixin_hooks.LevelChunkAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelChunk.class)
public class LevelChunkMixin implements LevelChunkAdditions {

    @Unique
    private final Long2IntMap positionTickChances = new Long2IntOpenHashMap();

    @Override
    public void runic_rituals$setRandomTickDelay(BlockPos pos, int chance){
        positionTickChances.put(pos.asLong(), chance);
    }

    @Override
    public Integer runic_rituals$getChance(BlockPos pos) {
        int chance = positionTickChances.getOrDefault(pos.asLong(), 0);
        if(chance != 0){
            positionTickChances.remove(pos.asLong());
        }
        return chance == 0 ? null : chance;
    }
}
