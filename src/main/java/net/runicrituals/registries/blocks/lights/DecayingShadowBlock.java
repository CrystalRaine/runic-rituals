package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import org.jspecify.annotations.NonNull;

public class DecayingShadowBlock extends ShadowBlock {

    private static final int DECAY_TIME = 20 * 60 * 2;

    public DecayingShadowBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(final @NonNull BlockState state, final Level level, final @NonNull BlockPos pos, final @NonNull BlockState oldState, final boolean movedByPiston) {
        level.scheduleTick(pos, this, DECAY_TIME, TickPriority.EXTREMELY_LOW);
    }

    @Override
    protected void tick(BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        int lightLevel = state.getValue(LEVEL);
        if(lightLevel == 0) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return;
        }
        level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(LEVEL, lightLevel-1));
        level.scheduleTick(pos, this, DECAY_TIME, TickPriority.EXTREMELY_LOW);
    }
}
