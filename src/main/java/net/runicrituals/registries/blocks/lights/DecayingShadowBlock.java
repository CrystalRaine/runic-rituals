package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.RunicRituals;
import org.jspecify.annotations.NonNull;

public class DecayingShadowBlock extends ShadowBlock {

    public DecayingShadowBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(final @NonNull BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        int lightLevel = state.getValue(LEVEL);
        if (lightLevel == 0) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return;
        }
        BlockState newState = this.defaultBlockState().setValue(LEVEL, lightLevel - 1);
        level.setBlock(pos, newState, Block.UPDATE_ALL);
    }
}
