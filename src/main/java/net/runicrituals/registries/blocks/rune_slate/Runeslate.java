package net.runicrituals.registries.blocks.rune_slate;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Runeslate extends BaseEntityBlock {

    private static final VoxelShape SLATE = net.minecraft.world.level.block.Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public Runeslate(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NonNull List<ItemStack> getDrops(final @NonNull BlockState state, LootParams.Builder params) {
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof RuneslateEntity runeslateEntity) {
            Item item = RunicRitualsBlocks.RUNESLATE.asItem();
            ItemStack is = new ItemStack(item, 1);
            for(TypedDataComponent<?> ct : runeslateEntity.components()) {
                is.set(ct);
            }
            return Collections.singletonList(is);
        }
        return super.getDrops(state, params);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack stack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hit) {
        RuneslateEntity rse = getBlockEntity(level, pos);

        if(player.getItemInHand(hand).is(RunicRitualsItems.BASIC_WAND)) {
            if(rse != null) {
                if(!rse.isLinked) {
                    rse.isAnchor = true;
                    rse.setAnchor(rse);

                    rse.link();
                    if(rse.chainIndex == 0) {
                        rse.getAnchor().delink();
                        player.sendOverlayMessage(Component.literal("Linking Ritual: Failed due to incomplete loop"));
                        return InteractionResult.SUCCESS;
                    }

                    boolean validBase = rse.getBase();
                    if(!validBase) {
                        rse.getAnchor().delink();
                        player.sendOverlayMessage(Component.literal("Linking Ritual: Failed due to being unable to validate contained volume"));
                        return InteractionResult.SUCCESS;
                    }

                    if(level.isClientSide()) {
                        player.sendOverlayMessage(Component.literal("Completed Ritual Circle, Size: " + (rse.chainIndex)));
                    }
                } else if(rse.getAnchor() != null){
                    rse.getAnchor().delink();
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static RuneslateEntity getBlockEntity(@NonNull Level level, @NonNull BlockPos pos) {
        BlockEntity re = level.getBlockEntity(pos);
        if(re instanceof RuneslateEntity) {
            return (RuneslateEntity) re;
        }
        return null;
    }

    @Override
    protected @NonNull VoxelShape getShape(final @NonNull BlockState state, final @NonNull BlockGetter level, final @NonNull BlockPos pos, final @NonNull CollisionContext context) {
        return SLATE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return createTickerHelper(type, RunicRitualsBlockEntities.RUNESLATE_BLOCK_ENTITY, RuneslateEntity::tick);
    }

    @Override
    public @NonNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NonNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(final @NonNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(Runeslate::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new RuneslateEntity(worldPosition, blockState);
    }
}
