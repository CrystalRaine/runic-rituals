package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.registries.RunicRitualsBlocks;

public class ShadowBlock extends LightBlock {

    public ShadowBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean propagatesSkylightDown(final BlockState state) {
        return false;
    }

    @Override
    protected int getLightDampening(final BlockState state) {
        return state.getValue(LEVEL);
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
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return context.isHoldingItem(Items.LIGHT) ||
                (RunicRitualsBlocks.SHADOW != null && context.isHoldingItem(RunicRitualsBlocks.SHADOW.asItem())) ||
                (RunicRitualsBlocks.DECAYING_LIGHT != null && context.isHoldingItem(RunicRitualsBlocks.DECAYING_LIGHT.asItem())) ||
                (RunicRitualsBlocks.DECAYING_SHADOW != null && context.isHoldingItem(RunicRitualsBlocks.DECAYING_SHADOW.asItem()))
                ? Shapes.block() : Shapes.empty();
    }
}