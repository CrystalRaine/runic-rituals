package net.runicrituals.registries.blocks.lights;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import org.jspecify.annotations.NonNull;

public class DecayingLightBlock extends LightBlock {

    private static final int DECAY_TIME = 20 * 60;

    public DecayingLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(final @NonNull BlockState state, final Level level, final @NonNull BlockPos pos, final @NonNull BlockState oldState, final boolean movedByPiston) {
        level.scheduleTick(pos, this, DECAY_TIME, TickPriority.EXTREMELY_LOW);
    }

    @Override
    protected void tick(BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        int lightLevel = state.getValue(LEVEL);
        if(lightLevel == 0) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return;
        }
        level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(LEVEL, lightLevel-1));
        level.scheduleTick(pos, this, DECAY_TIME, TickPriority.EXTREMELY_LOW);
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