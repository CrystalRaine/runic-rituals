package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.runicrituals.RunicRituals;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

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

    @Override
    protected @NonNull BlockState updateShape(
            final BlockState state,
            final @NonNull LevelReader level,
            final @NonNull ScheduledTickAccess ticks,
            final @NonNull BlockPos pos,
            final @NonNull Direction direction,
            final @NonNull BlockPos neighbourPos,
            final @NonNull BlockState neighbour,
            final @NonNull RandomSource random
    ) {
        if (!state.getValue(WATERLOGGED)) {
            FluidState neighborFluid = neighbour.getFluidState();
            if (!neighborFluid.isEmpty()) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbour, random);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }
}
