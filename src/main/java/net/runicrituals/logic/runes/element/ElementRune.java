package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;
import net.runicrituals.logic.runes.action.ActionRune;

/**
 * Element runes define the expression of a rune sequence.
 * generally speaking, you should extend ElementRune for any elemental rune, and the implementation should:
 *      - implement applyAction (both overrides) to handle how entities and blocks are effected by the ritual
 *          - in the implementations, there should be a switch statement for each action type supported.
 *      - implement createParticle to create an appropriate particle for each action supported
 */
public abstract class ElementRune extends Rune {

    @Override
    public RuneType getType() {
        return RuneType.ELEMENT;
    }

//    do nothing by default
    public void applyAction(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence){};

    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence){};

    public abstract void createParticle(Level level, BlockPos pos, ActionRune action);

    public void updateIntensity(ActionRune action, RuneSequence sequence) {}

    public void destroyBlock(Level level, BlockPos pos, BlockState block) {
        if(!level.isClientSide()) {
            BlockState old = level.getBlockState(pos);
            level.destroyBlock(pos, false);
            level.sendBlockUpdated(pos, old, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public void setBlock(Level level, BlockPos pos, Block block) {
        if(!level.isClientSide()){
            level.setBlockAndUpdate(pos, block.defaultBlockState());
        }
    }

    public void replaceBlock(Level level, BlockPos pos, Block replaced, Block replaceWith) {
        if(!level.isClientSide() && level.getBlockState(pos).is(replaced)){
            level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
        }
    }

    public void replaceBlock(Level level, BlockPos pos, TagKey<Block> replaced, Block replaceWith) {
        if(!level.isClientSide() && level.getBlockState(pos).is(replaced)){
            level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
        }
    }
}
