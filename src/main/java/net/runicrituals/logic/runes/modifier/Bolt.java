package net.runicrituals.logic.runes.modifier;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.registries.RunicRitualsEntities;
import net.runicrituals.registries.entities.RitualBolt;

public class Bolt extends ModifierRune{
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        if(runeslateEntity.getLevel() == null) return CastTrigger.KILL_EXECUTION;
        if(runeslateEntity.getLevel().isClientSide()) return CastTrigger.KILL_EXECUTION;
        
        RitualBolt rb = RunicRitualsEntities.RITUAL_BOLT.create(runeslateEntity.getLevel(), EntitySpawnReason.TRIGGERED);
        
        if(rb == null) return CastTrigger.KILL_EXECUTION;
        
        Entity e = castingBlock.getForm().getBoundEntity();
        if(!(e instanceof LivingEntity livingEntity)) {
            
            Projectile.spawnProjectile(
                rb,
                (ServerLevel) runeslateEntity.getLevel(),
                new ItemStack(Items.AMETHYST_SHARD),
                projectileEntity -> {
                    float xd = -Mth.sin(90 * (float) (Math.PI / 180.0)) * Mth.cos(0 * (float) (Math.PI / 180.0));
                    float yd = -Mth.sin((0) * (float) (Math.PI / 180.0));
                    float zd = Mth.cos(90 * (float) (Math.PI / 180.0)) * Mth.cos(0 * (float) (Math.PI / 180.0));
                    projectileEntity.shoot(xd, yd, zd, 3, 1);
                }
            );
            rb.teleportTo(castingBlock.getForm().getPosition().x(), castingBlock.getForm().getPosition().y(), castingBlock.getForm().getPosition().z());
            
        } else {
            Projectile.spawnProjectile(
                rb,
                (ServerLevel) runeslateEntity.getLevel(),
                new ItemStack(Items.AMETHYST_SHARD),
                projectileEntity -> projectileEntity.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, 3, 1)
            );
            rb.teleportTo(livingEntity.getEyePosition().x(), livingEntity.getEyePosition().y(), livingEntity.getEyePosition().z());
            
        }
        rb.setTriggerLocation(runeslateEntity.getBlockPos());
        return CastTrigger.BOLT_HIT;
    }
    
    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }
    
    @Override
    public String name() {
        return "Bolt";
    }
}
