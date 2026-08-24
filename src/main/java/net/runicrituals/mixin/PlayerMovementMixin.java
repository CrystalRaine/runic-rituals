package net.runicrituals.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.mixin_hooks.PlayerAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMovementMixin implements PlayerAdditions {

    @Unique
    double deltaScale = 1;

    @Unique
    long boostEndTimestamp = Long.MAX_VALUE;

    @Inject(method = "travel", at = @At("HEAD"))
    private void scaleDelta(Vec3 input, CallbackInfo ci) {
//        Vec3 movement = ((Player) (Object) this).getDeltaMovement();
//        ((Player) (Object) this).setDeltaMovement(movement.scale(deltaScale));
//
//        if(System.currentTimeMillis() >= boostEndTimestamp)
//            runic_rituals$setDeltaScale(1, Long.MAX_VALUE);
    }

//    all boost changes override previous ones.
    @Override
    public void runic_rituals$setDeltaScale(double ds, long unixTimeMovementChangeEnds) {
        boostEndTimestamp = unixTimeMovementChangeEnds;
        deltaScale = ds;
    }
}
