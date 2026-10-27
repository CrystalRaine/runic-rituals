package net.runicrituals.logic.runes.modifier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;

public class Bound extends ModifierRune {

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Bound";
    }

    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {

        Vec3 pos = runeslateEntity.getPositionComponentPosition();
        Entity entity = runeslateEntity.getBoundEntityTarget();
        
        if(pos != null) {
            castingBlock.getForm().setPosition(pos);
        }
        if(entity != null) {
            castingBlock.getForm().setBoundEntity(entity);
        } else {
            castingBlock.getForm().setBoundEntity(null);
        }

        runeslateEntity.removeControlComponents();
        
        if(pos == null) {
            return CastTrigger.KILL_EXECUTION;
        }
        return null;
    }
}
