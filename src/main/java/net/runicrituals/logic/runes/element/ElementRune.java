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

import static net.runicrituals.logic.RuneSymbol.MANIFEST;
import static net.runicrituals.logic.RuneSymbol.SACRIFICE;

/**
 * Element runes define the expression of a rune sequence.
 * generally speaking, you should extend ElementRune for any elemental rune, and the implementation should:
 *      - implement applyAction to handle how entities and blocks are effected by the ritual
 *      - implement createParticle to create an appropriate particle for each action supported
 *      - implement propose cost gets the mana cost of the corresponding applyAction function
 *      - implement updateIntensity to let a rune update it's (and subsequent runes) entities
 */
public abstract class ElementRune extends Rune {

    @Override
    public RuneType getType() {
        return RuneType.ELEMENT;
    }

    public void applyAction(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence) {
    };

    public void applyAction(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
    };

    public double proposeCost(Level level, BlockPos position, ActionRune action, RuneSequence runningSequence) {
        return 0;
    };

    public double proposeCost(Level level, Entity entity, ActionRune action, RuneSequence runningSequence) {
        return 0;
    };

    public double proposeCost(ActionRune action, RuneSequence runningSequence) {
        return 0;
    }

    public abstract void createParticle(Level level, BlockPos pos, ActionRune action);

    public double updateIntensity(ActionRune action, double intensity) {return intensity;}

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

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    protected double defaultCosts(ActionRune action) {
        switch (action.getActionType()) {
            case SACRIFICE -> {
                return BASE_RUNE_MANA_COST * invertEfficiency();
            }
            case MANIFEST -> {
                return BASE_RUNE_MANA_COST * efficiency();
            }
        }
        return 0;
    }
}
