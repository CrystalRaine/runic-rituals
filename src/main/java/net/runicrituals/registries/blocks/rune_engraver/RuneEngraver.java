package net.runicrituals.registries.blocks.rune_engraver;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsStats;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * well. this was a pain to set up lol
 */
public class RuneEngraver extends HorizontalDirectionalBlock {

    private static final VoxelShape TABLE_TOP = Block.box(0.0, 12.0, 0.0, 16.0, 15.0, 16.0);
    private static final VoxelShape TABLE_LEG_LL = Block.box(0.0, 0.0, 0.0, 2.0, 12.0, 2.0);
    private static final VoxelShape TABLE_LEG_LR = Block.box(0.0, 0.0, 14.0, 2.0, 12.0, 16.0);
    private static final VoxelShape TABLE_LEG_RL = Block.box(14.0, 0.0, 0.0, 16.0, 12.0, 2.0);
    private static final VoxelShape TABLE_LEG_RR = Block.box(14.0, 0.0, 14.0, 16.0, 12.0, 16.0);

    private static final VoxelShape L_LEGS = Shapes.or(TABLE_LEG_LR, TABLE_LEG_LL);
    private static final VoxelShape R_LEGS = Shapes.or(TABLE_LEG_RR, TABLE_LEG_RL);
    private static final VoxelShape LEGS = Shapes.or(R_LEGS, L_LEGS);
    private static final VoxelShape TABLE = Shapes.or(TABLE_TOP, LEGS);

    private static final MapCodec<RuneEngraver> CODEC = simpleCodec(RuneEngraver::new);

    public RuneEngraver(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull VoxelShape getShape(final @NonNull BlockState state, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final @NonNull CollisionContext context) {
        return TABLE;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hit) {

        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(RunicRitualsStats.INTERACT_WITH_RUNE_ENGRAVERS);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new RuneEngraverMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), this.getName());
    }
}
