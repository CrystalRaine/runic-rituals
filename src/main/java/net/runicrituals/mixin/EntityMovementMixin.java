package net.runicrituals.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.mixin_hooks.EntityAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Entity.class)
public class EntityMovementMixin implements EntityAdditions {

    @Unique
    double deltaScale = 1;

    @Unique
    long boostEndTimestamp = 0;

    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true, name = "delta")
    private Vec3 scaleDelta(Vec3 delta) {

        Vec3 newDelta = delta.scale(deltaScale);

        if(System.currentTimeMillis() >= boostEndTimestamp)
            runic_rituals$setDeltaScale(1, Long.MAX_VALUE);

        return newDelta;
    }

//    all boost changes override previous ones.
    @Override
    public void runic_rituals$setDeltaScale(double ds, long unixTimeMovementChangeEnds) {
        boostEndTimestamp = unixTimeMovementChangeEnds;
        deltaScale = ds;
    }
}
