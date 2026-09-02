package net.runicrituals.mixin_hooks;

import net.minecraft.core.BlockPos;

public interface LevelChunkAdditions {

    default void runic_rituals$setRandomTickDelay(BlockPos pos, int chance){throw new IllegalStateException("Implemented by Mixin");}

    default Integer runic_rituals$getChance(BlockPos pos) {throw new IllegalStateException("Implemented by Mixin");}
}
