package net.runicrituals.logic.runes.form;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.RuneType;
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
public abstract class FormRune extends Rune {

    Level level;
    private List<BlockPos> validTargets = new ArrayList<>();

    BlockPos actionLocation;
    float radius;

    public void setDefaultRadius(){
        radius = 4;
    }

    public void setProperties(Level level, BlockPos actionLocation, float radius) {
        this.level = level;
        this.actionLocation = actionLocation;
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

        if(validTargets.isEmpty()) {
            validTargets = getAllBlocks()
                .filter(b -> RitualAnchor.getBlockEntity(level, b) == null && Runeslate.getBlockEntity(level, b) == null)
                .toList();
        }

        BlockPos pos = getRandom(level.getRandom(), validTargets);

        return pos;
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

    public abstract String name();
}
