package net.runicrituals.logic.runes.modifier;

import net.minecraft.core.BlockPos;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneType;

public class Static extends ModifierRune {

    @Override
    public RuneType getType() {
        return RuneType.MODIFIER;
    }

    @Override
    public double applyEfficiencyToCost(double cost) {
        return cost;
    }

    @Override
    public String name() {
        return "Static";
    }
    
    
    //TODO: currently identical behavior to Bound rune. i want this to be changed however, to take the last cast location, and use that
    @Override
    public CastTrigger preformModification(CastingBlock castingBlock) {
        
        BlockPos pos = runeslateEntity.getPositionComponentPosition();
        if(pos != null) {
            castingBlock.getForm().setPosition(pos);
        }
        
        runeslateEntity.removeControlComponents();
        
        if(pos == null) {
            return CastTrigger.KILL_EXECUTION;
        }
        return null;
    }
}
