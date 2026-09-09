package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.mixin_hooks.EntityAdditions;

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
     * apply this rune's action on the entire volume
     *
     * @param level           current level
     * @param form
     * @param action          the action to take
     * @param block currently running casting block
     */
    public void applyActionOnVolume(Level level, FormRune form, ActionRune action, CastingBlock block) {
    };

    /**
     * apply this rune's action on a block
     *
     * @param level           current level
     * @param form
     * @param actAt           the block position to act on
     * @param action          the action to take
     * @param block  currently running casting block
     */
    public void applyActionOnBlock(Level level, FormRune form, BlockPos actAt, ActionRune action, CastingBlock block) {
    };

    /**
     * apply this rune's action on an entity
     * @param level current level
     * @param entity the entity to act on
     * @param action the action to take
     * @param block  currently running casting block
     */
    public void applyActionOnEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
    };

    /**
     * propose a cost for using this rune's action on a block
     *
     * @param level           current level
     * @param form
     * @param position        the block position to act on
     * @param action          the action to take
     * @param block currently running casting block
     * @return cost to act on this block
     */
    public double proposeCostForBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block) {
        return 0;
    };

    /**
     * propose a cost for using this rune's action on an entity
     * @param level current level
     * @param entity the entity to act on
     * @param action the action to take
     * @param block currently running casting block
     * @return cost to act on this entity
     */
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
        return 0;
    };

    /**
     * propose a cost to change this ritual's intensity
     * @param action action being taken
     * @param block the current running casting block
     * @return cost to update the rune sequence's intensity
     */
    public double proposeCostForIntensityChange(ActionRune action, CastingBlock block) {
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
     * @param replaceWith block to set in its place
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
        return 0;
    }

    static void scaleEntityMotion(EntityAdditions entity, ActionRune action, CastingBlock block) {

        switch (action.getActionType()) {
            case MANIFEST -> entity.runic_rituals$setDeltaScale(block.intensity + 1);
            case SACRIFICE -> entity.runic_rituals$setDeltaScale(1 / (block.intensity + 1));
        }
    }

    static void createParticle(Level level, BlockPos pos, ParticleOptions particleType, Vec3 velocityScaler) {
        RandomSource random = level.getRandom();
        level.addParticle(
                particleType,
                pos.getX() + Mth.randomBetween(random, 0, 1.0F),
                pos.getY() + Mth.randomBetween(random, 0, 1.0F),
                pos.getZ() + Mth.randomBetween(random, 0, 1.0F),
                Mth.randomBetween(random, -1.0F, 1.0F) * velocityScaler.x(),
                Mth.randomBetween(random, -1.0F, 1.0F) * velocityScaler.y(),
                Mth.randomBetween(random, -1.0F, 1.0F) * velocityScaler.z()
        );
    }
}
