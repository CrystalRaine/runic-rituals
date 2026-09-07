package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;

public class Electric extends ElementRune{


    @Override
    public double proposeCostForBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block) {

        if(action.getActionType() != ActionRune.Action.MANIFEST) {
            return Double.POSITIVE_INFINITY;
        }

        BlockPos pos = position;

        while (level.getBlockState(pos.below()).is(Blocks.AIR) && form.isPositionInVolume(pos)) {
            pos = pos.below();
        }

        if(form.isPositionInVolume(pos) && level.getBlockState(pos).is(Blocks.AIR) && !level.getBlockState(pos.below()).is(Blocks.AIR)) {
            return BASE_RUNE_MANA_COST * 100 * efficiency();
        }

        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void applyActionOnBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block) {

        if(action.getActionType() != ActionRune.Action.MANIFEST) {
            return;
        }

//        Drop to ground
        BlockPos pos = position;

        while (level.getBlockState(pos.below()).is(Blocks.AIR) && form.isPositionInVolume(pos)) {
            pos = pos.below();
        }

        if(form.isPositionInVolume(pos) && level.getBlockState(pos).is(Blocks.AIR) && !level.getBlockState(pos.below()).is(Blocks.AIR)) {
            LightningBolt lightningBolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
            if(lightningBolt != null) {
                lightningBolt.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                level.addFreshEntity(lightningBolt);
            }
        }
    }


    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {

        if(entity instanceof LightningBolt || entity instanceof ItemEntity || action.getActionType() != ActionRune.Action.MANIFEST || block.intensity < 3) {
            return Double.POSITIVE_INFINITY;
        }

        return BASE_RUNE_MANA_COST * 95 * efficiency();
    }

    @Override
    public void applyActionOnEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {

        if(entity instanceof LightningBolt || entity instanceof ItemEntity || action.getActionType() != ActionRune.Action.MANIFEST || block.intensity < 3) {
            return;
        }

        Vec3 entityPos = entity.position();
        LightningBolt lightningBolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
        if(lightningBolt != null) {
            lightningBolt.teleportTo(entityPos.x, entityPos.y, entityPos.z);
            level.addFreshEntity(lightningBolt);
        }
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        RandomSource random = level.getRandom();
        level.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
                0.05F,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
        );
    }
}
