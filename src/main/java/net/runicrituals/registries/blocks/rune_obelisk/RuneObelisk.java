package net.runicrituals.registries.blocks.rune_obelisk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.RunicRitualsStats;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


/**
 * yoink double plant block for double-block logic, but override the restricted blocks it can be placed on
 */
public class RuneObelisk extends BaseEntityBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public static final VoxelShape OBELISK_BOTTOM = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    private static final VoxelShape LAYER_1 = Block.box(4.0, 11.0, 4.0, 12.0, 12.0, 12.0);
    private static final VoxelShape LAYER_2 = Block.box(5.0, 12.0, 5.0, 11.0, 13.0, 11.0);
    private static final VoxelShape LAYER_3 = Block.box(6.0, 13.0, 6.0, 10.0, 14.0, 10.0);
    private static final VoxelShape LAYER_4 = Block.box(7.0, 14.0, 7.0, 9.0, 15.0, 9.0);

    private static final VoxelShape OBELISK_TOP_PYRAMID_1 = Shapes.or(LAYER_1, LAYER_2);
    private static final VoxelShape OBELISK_TOP_PYRAMID_2 = Shapes.or(LAYER_3, LAYER_4);

    private static final VoxelShape OBELISK_TOP_PYRAMID = Shapes.or(OBELISK_TOP_PYRAMID_1, OBELISK_TOP_PYRAMID_2);
    private static final VoxelShape OBELISK_TOP_BASE = Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);
    public static final VoxelShape OBELISK_TOP = Shapes.or(OBELISK_TOP_BASE, OBELISK_TOP_PYRAMID);

    public static final MapCodec<RuneObelisk> CODEC = simpleCodec(RuneObelisk::new);

    public RuneObelisk(Properties properties) {
        super(properties);

        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, RunicRitualsBlockEntities.RUNE_OBELISK_ENTITY_BLOCK_ENTITY, RuneObeliskEntity::tick);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack stack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hit) {
        RuneObeliskEntity roe = getBlockEntity(level, pos, state);

        if(player.getItemInHand(hand).is(RunicRitualsItems.BASIC_WAND)) {
            if(roe != null) {
                roe.toggleActive();
                if (level.isClientSide()) {
                    player.sendOverlayMessage(Component.literal(roe.getActive() ? "Active" : "Inactive"));
                } else if(roe.getActive()){
                    roe.addMana(10);
                }
            }
        } else {
            if (!level.isClientSide() && roe != null) {
                player.openMenu(roe);
                player.awardStat(RunicRitualsStats.INTERACT_WITH_RUNE_OBELISKS);
            }
        }

        return InteractionResult.SUCCESS;
    }


    private RuneObeliskEntity getBlockEntity(Level level, BlockPos pos, BlockState state) {
//        Double block container, so always use the bottom block's inventory so they aren't different
//        depending on which part of the block you click on.
        DoubleBlockHalf half = state.getValue(HALF);
        BlockEntity be;
        if(half == DoubleBlockHalf.LOWER) {
            be = level.getBlockEntity(pos);
        } else {
            be = level.getBlockEntity(pos.below());
        }

        if(!(be instanceof RuneObeliskEntity)){
            return null;
        }
        return (RuneObeliskEntity) be;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
//        only bottom half has a blockEntity, so only create that half.
//        via getBlockEntity, only the bottom half's entity will be accessed.
        DoubleBlockHalf half = blockState.getValue(HALF);
        if(half == DoubleBlockHalf.LOWER) {
            return new RuneObeliskEntity(worldPosition, blockState);
        } else {
            return null;
        }
    }

//    Below this is basically letting the multi-tile nonsense work. a lot of this is also modified
//    double flower code
    @Override
    public @NonNull MapCodec<? extends RuneObelisk> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull BlockState updateShape(
            final BlockState state,
            final @NonNull LevelReader level,
            final @NonNull ScheduledTickAccess ticks,
            final @NonNull BlockPos pos,
            final Direction directionToNeighbour,
            final @NonNull BlockPos neighbourPos,
            final @NonNull BlockState neighbourState,
            final @NonNull RandomSource random
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
}
