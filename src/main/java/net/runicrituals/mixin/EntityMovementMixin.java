package net.runicrituals.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.mixin_hooks.EntityAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Entity.class)
public class EntityMovementMixin implements EntityAdditions {

    @Unique
    double deltaScale = 1;

    @Unique
    double boostEndTimestamp = Long.MAX_VALUE;

    @Shadow
    private Level level;

    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true, name = "delta")
    private Vec3 scaleDelta(Vec3 delta) {

        if(!level.isClientSide())
            return delta;

        Vec3 newDelta = delta.scale(deltaScale);

        if(System.currentTimeMillis() >= boostEndTimestamp)
            runic_rituals$setDeltaScale(1, Long.MAX_VALUE);

        return newDelta;
    }

    @Override
    public double runic_rituals$getDeltaScale() {
        return deltaScale;
    }

    @Override
    public void runic_rituals$setDeltaScale(double ds, double unixTimeMovementChangeEnds) {
        deltaScale = ds;
        boostEndTimestamp = unixTimeMovementChangeEnds;
    }
}
