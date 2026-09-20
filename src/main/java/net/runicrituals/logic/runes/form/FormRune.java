package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.runicrituals.logic.Util.blockPosToVec3;
import static net.runicrituals.logic.Util.getRandom;


/**
 * provides default behavior for FormRunes, as well as an interface for them to be used.
 * basic forms will likely only need to override (at most) name(), getAABB(), getAllBlocks(), and getEntities()
 * cubic forms will not need to override getEntities(), or getAllBlocks(), as they will be fine with the default
 * as they use getAABB as a base definition.
 * non-cubic forms should override getAABB to be the encompassing bounding box, and should then override the above noted
 * methods in order to filter that volume.
 */
public abstract class FormRune extends Rune implements Cloneable{

    Level level;

    BlockPos actionLocation;
    float radius;

    public void setProperties(Level level, float radius) {
        this.level = level;
        this.radius = radius;
    }

    public AABB getAABB() {
        return new AABB(actionLocation).inflate(Math.max(0, radius - 0.5f));
    }

    public Stream<BlockPos> getAllBlocks() {
        return BlockPos.betweenClosedStream(getAABB()).map(BlockPos::immutable);
    }

    public Stream<BlockPos> getAllNonAirBlocks() {
        return getAllBlocks().filter(b -> !level.getBlockState(b).is(Blocks.AIR));
    }

    public BlockPos getBlockTarget() {

        List<BlockPos> validTargets = getAllBlocks()
                .filter(b -> RitualAnchor.getBlockEntity(level, b) == null && Runeslate.getBlockEntity(level, b) == null)
                .toList();

        return getRandom(level.getRandom(), validTargets);
    }

    public boolean isPositionInVolume(BlockPos pos) {
        return getAABB().contains(blockPosToVec3(pos));
    }

    public List<Entity> getTargetEntities() {
        return level.getEntities(null, getAABB());
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public RuneType getType() {
        return RuneType.FORM;
    }

    public void setPosition(BlockPos actionLocation) {
        this.actionLocation = actionLocation;
    }

    public BlockPos getPosition() {
        return actionLocation;
    }

    @Override
    public FormRune clone() {
        try {
            FormRune clone = (FormRune) super.clone();
            clone.actionLocation = new BlockPos(actionLocation.getX(), actionLocation.getY(), actionLocation.getZ());
            clone.radius = radius;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
