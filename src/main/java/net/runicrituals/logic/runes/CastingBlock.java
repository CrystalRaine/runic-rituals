package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;

import java.util.ArrayList;
import java.util.List;

public class CastingBlock {

    private class ActionNode {
        private ActionRune action;

        private final List<ElementRune> elements = new ArrayList<>();

        protected double getCost() {
            if(elements.isEmpty()) return 0;
            double elementCostTotal = elements.stream().map(e -> e.applyEfficiencyToCost(RuneSequence.BASE_SEQUENCE_COST)).reduce(Double::sum).get();
            return action.applyEfficiencyToCost(elementCostTotal);
        }
    }

    FormRune form;

    List<ActionNode> actions = new ArrayList<>();

    public CastingBlock(FormRune form) {
        this.form = form;
    }

    public FormRune getForm() {
        return form;
    }

    public void getIntensity(RuneSequence sequence) {
        for(ActionNode actionNode : actions) {
            for(ElementRune element : actionNode.elements) {
                element.updateIntensity(actionNode.action, sequence);
            }
        }
    }

    public void cast(Level level, Position initialPos, RuneSequence sequence) {

        for(ActionNode actionNode : actions) {
            for(ElementRune element : actionNode.elements) {
                List<Entity> selectedEntities = form.getTargetEntities(level, initialPos);

                if(!level.isClientSide()) {
                    for (int i = 0; i < sequence.intensity; i++) {
                        BlockPos targetBlock = form.getTargetBlock(level, initialPos);
                        if(targetBlock != null) {
                            element.applyAction(level, targetBlock, actionNode.action, sequence);
                        }
                    }
                    for (Entity e : selectedEntities) {
                        element.applyAction(level, e, actionNode.action, sequence);
                    }
                } else {
                    BlockPos targetBlock = form.getTargetBlock(level, initialPos);
                    if(targetBlock != null) {
                        element.createParticle(level, targetBlock, actionNode.action);
                    }
                }
            }
        }
    }

    public double getManaCost() {
        if(actions.isEmpty()) return 0;
        double actionCost = actions.stream().map(ActionNode::getCost).reduce(Double::sum).get();
        return form.applyEfficiencyToCost(actionCost);
    }

    public void addAction(ActionRune action) {
        ActionNode actionNode = new ActionNode();
        actionNode.action = action;
        actions.add(actionNode);
    }

    public void addElement(ElementRune e) {
        if(actions.isEmpty()) return;

        actions.getLast().elements.add(e);
    }

    public boolean isCastable() {
        return form != null && !actions.isEmpty() && !actions.getFirst().elements.isEmpty();
    }
}
