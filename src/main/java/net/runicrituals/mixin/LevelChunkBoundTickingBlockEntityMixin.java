package net.runicrituals.mixin;


import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.runicrituals.mixin_hooks.BlockEntityAdditions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.chunk.LevelChunk$BoundTickingBlockEntity")
public class LevelChunkBoundTickingBlockEntityMixin<T extends BlockEntity> {

    @Shadow
    @Final
    private T blockEntity;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    void tickAddition(CallbackInfo ci) {
        int extraTicks = ((BlockEntityAdditions)blockEntity).runic_rituals$getExtraTicks();

        if(extraTicks == 0) {
            return;
        }

        if(extraTicks > 0) {
            // reset extra ticks first, so we avoid stack overflows / even more ticks
            ((BlockEntityAdditions)blockEntity).runic_rituals$resetExtraTicks();
            for(int i = 0; i < extraTicks; i++) {
                ((TickingBlockEntity)this).tick();
            }
        }

        if(extraTicks < 0) {
            ((BlockEntityAdditions)blockEntity).runic_rituals$resetExtraTicks();
            ci.cancel();
        }
    }
}
