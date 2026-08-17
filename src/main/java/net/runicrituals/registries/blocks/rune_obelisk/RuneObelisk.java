package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;


/**
 * yoink double plant block for double-block logic, but override the restricted blocks it can be placed on
 */
public class RuneObelisk extends DoublePlantBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape OBELISK_BOTTOM = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape OBELISK_TOP = Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);

    public RuneObelisk(Properties properties) {
        super(properties);

        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    @Override
    protected boolean mayPlaceOn(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return true;
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
