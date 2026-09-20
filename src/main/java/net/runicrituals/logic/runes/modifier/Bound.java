package net.runicrituals.logic.runes.modifier;

import net.minecraft.core.BlockPos;
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

        BlockPos pos = runeslateEntity.getPositionComponentPosition();
        if(pos != null) {
            castingBlock.getForm().setPosition(pos);
        }
        return null;
    }
}
