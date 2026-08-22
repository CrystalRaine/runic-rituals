package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
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
import net.runicrituals.logic.runes.form.FormRune;

/**
 * Element runes define the expression of a rune sequence.
 * generally speaking, you should extend ElementRune for any elemental rune, and the implementation should:
 *      - implement applyAction to handle how entities and blocks are effected by the ritual
 *      - implement createParticle to create an appropriate particle for each action supported
 *      - implement propose cost gets the mana cost of the corresponding applyAction function
 *      - implement updateIntensity to let a rune update it's (and subsequent runes) entities
 */
public abstract class ElementRune extends Rune {

    public ElementRune() {
    }

    public ElementRune(boolean canRunClientSide) {
        super(canRunClientSide);
    }

    @Override
    public RuneType getType() {
        return RuneType.ELEMENT;
    }

    /**
     * apply this rune's action on a block
     *
     * @param level           current level
     * @param form
     * @param ritualCenter    center of the ritual
     * @param actAt           the block position to act on
     * @param action          the action to take
     * @param runningSequence currently running rune sequence
     */
    public void applyAction(Level level, FormRune form, Position ritualCenter, BlockPos actAt, ActionRune action, RuneSequence runningSequence) {
    };

    /**
     * apply this rune's action on an entity
     * @param level current level
     * @param entity the entity to act on
     * @param action the action to take
     * @param runningSequence currently running rune sequence
     */
    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
    };

    /**
     * propose a cost for using this rune's action on a block
     *
     * @param level           current level
     * @param form
     * @param initialPos      position of the ritual's center
     * @param position        the block position to act on
     * @param action          the action to take
     * @param runningSequence currently running rune sequence
     * @return cost to act on this block
     */
    public double proposeCostForBlock(Level level, FormRune form, Position initialPos, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        return 0;
    };

    /**
     * propose a cost for using this rune's action on an entity
     * @param level current level
     * @param entity the entity to act on
     * @param action the action to take
     * @param runningSequence currently running rune sequence
     * @return cost to act on this entity
     */
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        return 0;
    };

    /**
     * propose a cost to change this ritual's intensity
     * @param action action being taken
     * @param runningSequence the current running rune sequence
     * @return cost to update the rune sequence's intensity
     */
    public double proposeCostForIntensityChange(ActionRune action, RuneSequence runningSequence) {
        return 0;
    }

    /**
     * create a particle in the world corresponding to this element
     * @param level the current level
     * @param pos position to create the particle
     * @param action action being taken
     */
    public abstract void createParticle(Level level, BlockPos pos, ActionRune action);

    /**
     * updates intensity of the rune sequence
     * @param action current action being taken
     * @param intensity current sequence intensity
     * @return the new intensity
     */
    public double updateIntensity(ActionRune action, double intensity) {return intensity;}

    /**
     * helper method to destroy a block at a given position. does nothing when run on client, but does update client with server's state.
     * @param level current level
     * @param pos blockPos to destroy
     */
    public void destroyBlock(Level level, BlockPos pos) {
        if(!level.isClientSide()) {
            BlockState old = level.getBlockState(pos);
            level.destroyBlock(pos, false);
            level.sendBlockUpdated(pos, old, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    /**
     * Sets a block into the world
     * @param level current level
     * @param pos position to place block
     * @param block block to set
     */
    public void setBlock(Level level, BlockPos pos, Block block) {
        if(!level.isClientSide()){
            level.setBlockAndUpdate(pos, block.defaultBlockState());
        }
    }

    /**
     * replaces a block with new block. `replaced` is the block that can be removed, and replaceWith is what is put in it's place. if `pos` is not `replaced` does nothing.
     * @param level current level
     * @param pos position to replace
     * @param replaced block that can be replaced
     * @param replaceWith block to set in it's place
     */
    public void replaceBlock(Level level, BlockPos pos, Block replaced, Block replaceWith) {
        if(!level.isClientSide() && level.getBlockState(pos).is(replaced)){
            level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
        }
    }

    /**
     * replaces a block with new block. `replaced` is the block that can be removed, and replaceWith is what is put in it's place. if `pos` is not `replaced` does nothing.
     * @param level current level
     * @param pos position to replace
     * @param replaced block that can be replaced
     * @param replaceWith block to set in it's place
     */
    public void replaceBlock(Level level, BlockPos pos, Block replaced, Block replaceWith, boolean sourceFlowing) {
        if(!level.isClientSide() && level.getBlockState(pos).is(replaced) && ((level.getBlockState(pos).getFluidState().isSource() && !sourceFlowing) || (!level.getBlockState(pos).getFluidState().isSource() && sourceFlowing))){
            level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
        }
    }

    /**
     * replaces a block with new block. `replaced` is the block set that can be removed, and replaceWith is what is put in it's place. if `pos` is not `replaced` does nothing.
     * @param level current level
     * @param pos position to replace
     * @param replaced block tag that can be replaced
     * @param replaceWith block to set in it's place
     */
    public void replaceBlock(Level level, BlockPos pos, TagKey<Block> replaced, Block replaceWith) {
        if(!level.isClientSide() && level.getBlockState(pos).is(replaced)){
            level.setBlockAndUpdate(pos, replaceWith.defaultBlockState());
        }
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    /**
     * get the default set of costs for this rune.
     * @param action action being taken
     * @return cost of this rune
     */
    protected double defaultCosts(ActionRune action) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                return BASE_RUNE_MANA_COST * invertEfficiency();
            }
            case MANIFEST -> {
                return BASE_RUNE_MANA_COST * efficiency();
            }
        }
        return Double.POSITIVE_INFINITY;
    }
}
