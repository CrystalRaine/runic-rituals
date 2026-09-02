package net.runicrituals.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.runicrituals.mixin_hooks.EntityAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    void blockEntityTicks(Entity entity, CallbackInfo ci) {
        if(((EntityAdditions)entity).runic_rituals$shouldSuppressNextTick()) {
            ((EntityAdditions)entity).runic_rituals$resetSuppressNextTick();
            ci.cancel();
        }
    }

}
