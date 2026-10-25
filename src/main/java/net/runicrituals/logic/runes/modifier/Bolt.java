package net.runicrituals.logic.runes.modifier;

import net.minecraft.core.BlockPos;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;

public class Bolt extends ModifierRune{
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        
        BlockPos pos = runeslateEntity.getPositionComponentPosition();
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
