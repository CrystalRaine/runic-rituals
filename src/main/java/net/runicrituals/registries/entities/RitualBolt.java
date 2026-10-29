package net.runicrituals.registries.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import org.jspecify.annotations.NonNull;

public class RitualBolt extends Arrow {
    
    BlockPos triggerPos;
    
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
        return new ItemStack(Items.AMETHYST_SHARD);
    }
    
    @Override
    protected void tickDespawn() {
        this.kill((ServerLevel) this.level());
    }
    
    @Override
    public void kill(@NonNull ServerLevel level) {
        hitEffect(this.position());
        super.kill(level);
    }
    
    private void hitEffect(Vec3 pos) {
        
//        level().explode(this, this.getX(), this.getY(), this.getZ(), 5, Level.ExplosionInteraction.TRIGGER);
        
        if(triggerPos == null) return;
        
        RuneslateEntity rse = Runeslate.getBlockEntity(level(), triggerPos);
        
        if(rse == null) return;
        
        rse.getAnchor().triggerCastingBlock(CastTrigger.BOLT_HIT, triggerPos, pos);
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        hitEffect(entity.position());
        
    }
    
    protected void onHitBlock(final BlockHitResult hitResult) {
        hitEffect(Vec3.atCenterOf(hitResult.getBlockPos()));
    }
    
    public void setTriggerLocation(BlockPos blockPos) {
        this.triggerPos = blockPos;
    }
}
