package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsBlocks;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class DecayingLightBlock extends LightBlock {

    public DecayingLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        int lightLevel = state.getValue(LEVEL);
        boolean waterlogged = state.getValue(WATERLOGGED);
        if (lightLevel == 0) {
            if(waterlogged) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
                return;
            }
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return;
        }
        BlockState newState = this.defaultBlockState().setValue(LEVEL, lightLevel - 1).setValue(WATERLOGGED, waterlogged);
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

    /**
     * could theoretically create a mixin to change LightBlock to use this method definition instead, but frankly i want to keep it
     * unobtrusive to other mods, and i don't expect this to be used much anyway.
     * this is needed though, since otherwise the 3 added light-type blocks would only be breakable when holding a "LightBlock", but not when holding the block itself.
     * @param state blockstate
     * @param level BlockGetter
     * @param pos BlockPos
     * @param context context
     * @return hitbox shape of the block
     */
//    also, the RunicRitualsBlocks.SHADOW != null IS actually required, and can't be simplified to true. the block is null on startup, and crashes if you don't check
    @Override
    protected @NonNull VoxelShape getShape(final @NonNull BlockState state, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final CollisionContext context) {
        return context.isHoldingItem(Items.LIGHT) ||
                (RunicRitualsBlocks.SHADOW != null && context.isHoldingItem(RunicRitualsBlocks.SHADOW.asItem())) ||
                (RunicRitualsBlocks.DECAYING_LIGHT != null && context.isHoldingItem(RunicRitualsBlocks.DECAYING_LIGHT.asItem())) ||
                (RunicRitualsBlocks.DECAYING_SHADOW != null && context.isHoldingItem(RunicRitualsBlocks.DECAYING_SHADOW.asItem()))
                ? Shapes.block() : Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }

}