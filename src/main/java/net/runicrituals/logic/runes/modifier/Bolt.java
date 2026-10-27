package net.runicrituals.logic.runes.modifier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.registries.RunicRitualsEntities;
import net.runicrituals.registries.entities.RitualBolt;

public class Bolt extends ModifierRune{
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        
        if(runeslateEntity.getLevel() == null) return CastTrigger.KILL_EXECUTION;
        
        RitualBolt rb = RunicRitualsEntities.RITUAL_BOLT.create(runeslateEntity.getLevel(), EntitySpawnReason.TRIGGERED);
        
        if(rb == null) return CastTrigger.KILL_EXECUTION;
        
        Entity e = castingBlock.getForm().getBoundEntity();
        if(!(e instanceof LivingEntity livingEntity)) {
            runeslateEntity.getLevel().addFreshEntity(rb);
            Vec3 pos = castingBlock.getForm().getPosition();
            rb.shoot(0, 5, 0, 3, 1f);
            rb.teleportTo(pos.x() + 0.5f, pos.y() + 0.5f, pos.z() + 0.5f);
            rb.setTriggerLocation(runeslateEntity.getBlockPos());
            
            return CastTrigger.BOLT_HIT;
        } else {
            Vec3 entityFacing = livingEntity.getForward();
            runeslateEntity.getLevel().addFreshEntity(rb);
            Vec3 pos = castingBlock.getForm().getPosition();
            rb.shoot(entityFacing.x(), entityFacing.y(), entityFacing.z(), 3, 1f);
            rb.teleportTo(pos.x(), pos.y() + 1.5, pos.z());
            rb.setTriggerLocation(runeslateEntity.getBlockPos());
            
            return CastTrigger.BOLT_HIT;
        }
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
