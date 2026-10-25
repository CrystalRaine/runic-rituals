package net.runicrituals.registries.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class RitualBolt extends Arrow {
    /**
     * Access widened by fabric-transitive-access-wideners-v1 to accessible
     *
     * @param type
     * @param level
     */
    public RitualBolt(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }
    
    @Override
    public boolean canUsePortal(final boolean ignorePassenger) {
        return false;
    }
    
    @Override
    protected @NonNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }
    
    @Override
    protected void tickDespawn() {
        this.discard();
    }
    
}
