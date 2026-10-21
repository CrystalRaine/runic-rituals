package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsBlocks;
import org.jspecify.annotations.NonNull;

public class DecayingLightBlock extends LightBlock {

    public DecayingLightBlock(Properties properties) {
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
}