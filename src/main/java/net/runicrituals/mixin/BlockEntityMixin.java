package net.runicrituals.mixin;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.runicrituals.mixin_hooks.BlockEntityAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements BlockEntityAdditions {

    @Unique
    private int extraTicks = 0;

    @Override
    public int runic_rituals$getExtraTicks() {
        return extraTicks;
    }

    @Override
    public void runic_rituals$setExtraTicks(int value) {
        if(extraTicks != 0) return;
        extraTicks = value;
    }

    @Override
    public void runic_rituals$resetExtraTicks() {
        if(extraTicks == 0) return;
        if(extraTicks > 0) extraTicks = 0;
        if(extraTicks < 0) extraTicks++;
    }
}
