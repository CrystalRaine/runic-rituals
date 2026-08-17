package net.runicrituals.registries.blocks.rune_obelisk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * yoink double plant block for double-block logic, but override the restricted blocks it can be placed on
 */
public class RuneObelisk extends BaseEntityBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape OBELISK_BOTTOM = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape OBELISK_TOP = Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);

    public static final MapCodec<RuneObelisk> CODEC = simpleCodec(RuneObelisk::new);

    public RuneObelisk(Properties properties) {
        super(properties);

        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    @Override
    public MapCodec<? extends RuneObelisk> codec() {
        return CODEC;
    }

    @Override
    protected BlockState updateShape(
            final BlockState state,
            final LevelReader level,
            final ScheduledTickAccess ticks,
            final BlockPos pos,
            final Direction directionToNeighbour,
            final BlockPos neighbourPos,
            final BlockState neighbourState,
            final RandomSource random
    ) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (directionToNeighbour.getAxis() != Direction.Axis.Y
                || half == DoubleBlockHalf.LOWER != (directionToNeighbour == Direction.UP)
                || neighbourState.is(this) && neighbourState.getValue(HALF) != half) {
            return half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        return pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    @Override
    public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, final @Nullable LivingEntity by, final ItemStack itemStack) {
        BlockPos abovePos = pos.above();
        level.setBlockAndUpdate(abovePos, copyWaterloggedFrom(level, abovePos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)));
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        }

        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    public static BlockState copyWaterloggedFrom(final LevelReader level, final BlockPos pos, final BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos)) : state;
    }

    @Override
    public void playerDestroy(
            final Level level, final Player player, final BlockPos pos, final BlockState state, final @Nullable BlockEntity blockEntity, final ItemStack destroyedWith
    ) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith);
    }

    protected static void preventDropFromBottomPart(final Level level, final BlockPos pos, final BlockState state, final Player player) {
        DoubleBlockHalf part = state.getValue(HALF);
        if (part == DoubleBlockHalf.UPPER) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is(state.getBlock()) && bottomState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState = bottomState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(bottomPos, blockState, 35);
                level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    public @NonNull BlockState playerWillDestroy(final Level level, final BlockPos pos, final BlockState state, final Player player) {
        if (!level.isClientSide() && (player.preventsBlockDrops() || !player.hasCorrectToolForDrops(state))) {
            preventDropFromBottomPart(level, pos, state, player);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected @NonNull VoxelShape getShape(final @NonNull BlockState state, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final @NonNull CollisionContext context) {
        DoubleBlockHalf half = state.getValue(HALF);
        if(half == DoubleBlockHalf.LOWER) {
            return OBELISK_BOTTOM;
        }
        return OBELISK_TOP;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return null;
    }
}
