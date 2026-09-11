package net.runicrituals.logic.runes.element;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.blocks.lights.DecayingLightBlock;
import net.runicrituals.registries.blocks.lights.DecayingShadowBlock;

public class Light extends ElementRune{

    private static final int INTENSITY_FOR_STATUS_EFFECTS = 15;

    @Override
    public double proposeCostForBlock(Level level, FormRune form, BlockPos position, ActionRune action, CastingBlock block) {
        BlockState beforeState = level.getBlockState(position);
        if(beforeState.is(RunicRitualsBlocks.DECAYING_SHADOW) || beforeState.is(RunicRitualsBlocks.DECAYING_LIGHT) || beforeState.is(Blocks.AIR)) {
            return defaultCosts(action) / 10;
        }
        return 0;
    }

    @Override
    public void applyActionOnBlock(Level level, FormRune form, BlockPos actAt, ActionRune action, CastingBlock castingBlock) {
        BlockState beforeState = level.getBlockState(actAt);
        int brightness = 0;
        boolean modifiable = false;

        if(beforeState.is(RunicRitualsBlocks.DECAYING_LIGHT)) {
            brightness = beforeState.getValue(DecayingLightBlock.LEVEL);
            modifiable = true;
        } else if (beforeState.is(RunicRitualsBlocks.DECAYING_SHADOW)) {
            brightness = -beforeState.getValue(DecayingShadowBlock.LEVEL);
            modifiable = true;
        } else if (beforeState.is(Blocks.AIR)) {
            modifiable = true;
        }

        int intensity = (int)castingBlock.intensity;
        switch(action.getActionType()) {
            case MANIFEST -> {
                if(brightness < intensity) {
                    brightness++;
                }
            }
            case SACRIFICE -> {
                if(brightness > -intensity) {
                    brightness--;
                }
            }
            default -> {}
        }

        if(modifiable) {
            level.setBlockAndUpdate(actAt, getDecayingLightBlockWithBrightness(Math.clamp(brightness, -15, 15)));
        }
    }

    @Override
    public double proposeCostForEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {
        if(entity instanceof Player && block.intensity > INTENSITY_FOR_STATUS_EFFECTS) {
            return defaultCosts(action) / 2;
        }
        return 0;
    }

    @Override
    public void applyActionOnEntity(Level level, Entity entity, ActionRune action, CastingBlock block) {

        if(entity instanceof Player && block.intensity > INTENSITY_FOR_STATUS_EFFECTS) {
            switch(action.getActionType()) {
                case MANIFEST -> {
                    ((Player) entity).addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,  3 * 20 * ((int) block.intensity - INTENSITY_FOR_STATUS_EFFECTS), 0, true, true));
                }
                case SACRIFICE -> {
                    ((Player) entity).addEffect(new MobEffectInstance(MobEffects.DARKNESS,  3 * 20 * ((int) block.intensity - INTENSITY_FOR_STATUS_EFFECTS), 0, true, true));
                }
                default -> {}
            }
        }
    }

    @Override
    public void createParticle(Level level, BlockPos pos, ActionRune action) {
        createParticle(level, pos, ParticleTypes.END_ROD, new Vec3(0.09, 0.05, 0.09));
    }

    private BlockState getDecayingLightBlockWithBrightness(int brightness) {
        if(brightness > 0) {
            return RunicRitualsBlocks.DECAYING_LIGHT.defaultBlockState().setValue(DecayingLightBlock.LEVEL, brightness);
        }

        if(brightness < 0) {
            return RunicRitualsBlocks.DECAYING_SHADOW.defaultBlockState().setValue(DecayingShadowBlock.LEVEL, -brightness);
        }

        return Blocks.AIR.defaultBlockState();
    }
}
