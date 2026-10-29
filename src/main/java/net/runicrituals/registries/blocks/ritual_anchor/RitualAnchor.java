package net.runicrituals.registries.blocks.ritual_anchor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static net.runicrituals.registries.blocks.rune_slate.Runeslate.SLATE;

public class RitualAnchor extends BaseEntityBlock {

    public RitualAnchor(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SLATE;
    }

    public static RitualAnchorEntity getBlockEntity(Level level, BlockPos pos) {
        if(level.getBlockEntity(pos) instanceof RitualAnchorEntity rae) {
            return rae;
        }
        return null;
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RitualAnchor::new);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, RunicRitualsBlockEntities.RITUAL_ANCHOR_BLOCK_ENTITY, RitualAnchorEntity::tick);
    }

    @Override
    public BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new RitualAnchorEntity(worldPosition, blockState);
    }
}
