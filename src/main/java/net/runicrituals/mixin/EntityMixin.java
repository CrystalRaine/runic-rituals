package net.runicrituals.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.mixin_hooks.EntityAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Entity.class)
public class EntityMixin implements EntityAdditions {

    @Unique
    double deltaScale = 1;

    @Unique
    boolean suppressNextTick = false;

    @Unique
    double boostEndTimestamp = Long.MAX_VALUE;

    @Shadow
    private Level level;

    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true, name = "delta")
    private Vec3 scaleDelta(Vec3 delta) {

        if(!level.isClientSide())
            return delta;

        Vec3 newDelta = delta.scale(deltaScale);

        if(level.getGameTime() >= boostEndTimestamp) {
            deltaScale = 1;
            boostEndTimestamp = Long.MAX_VALUE;
        }
        return newDelta;
    }

    @Override
    public void runic_rituals$setDeltaScale(double ds) {
        deltaScale = ds;
        boostEndTimestamp = level.getGameTime() + 1;
    }

    @Override
    public void runic_rituals$suppressNextTick() {
        suppressNextTick = true;
    }

    @Override
    public void runic_rituals$resetSuppressNextTick() {
        suppressNextTick = false;
    }

    @Override
    public boolean runic_rituals$shouldSuppressNextTick() {
        return suppressNextTick;
    }
}
