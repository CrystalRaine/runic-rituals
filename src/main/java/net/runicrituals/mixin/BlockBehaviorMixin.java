package net.runicrituals.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.runicrituals.mixin_hooks.LevelChunkAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public class BlockBehaviorMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    void shortOutRandomTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        LevelChunkAdditions lca = ((LevelChunkAdditions)level.getChunkAt(pos));
        Integer chance = lca.runic_rituals$getChance(pos);
        if(chance != null) {
            int r = random.nextInt(chance);
            if (r != 0) {
                ci.cancel();
            }
        }
    }
}
