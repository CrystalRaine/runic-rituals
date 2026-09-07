package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskRuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.logic.ModifierRune;

import java.util.ArrayList;
import java.util.List;

public class CastingBlock {


    private List<ModifierRune> formModifiers;
    private final FormRune form;
    private final List<ActionNode> actions = new ArrayList<>();
    public double intensity = 0;

    private static class ActionNode {
        private ActionRune action;
        private final List<ElementRune> elements = new ArrayList<>();
    }


    public CastingBlock(FormRune form) {
        this.form = form;
    }

    public void setFormModifiers(List<ModifierRune> formModifiers) {
        this.formModifiers = formModifiers;
    }

    public void applyModifiers() {
        applyFormModifiers();
    }

    private void applyFormModifiers() {
        for(ModifierRune modifier : formModifiers) {
            modifier.applyModification(form);
        }
    }


    public double proposeManaCost(Level level) {
        if(!this.isCastable()) return Double.POSITIVE_INFINITY;

        double actionCostSum = 0;
        intensity = RuneSequence.BASE_INTENSITY;

        for(ActionNode actionNode : actions) {
            double elementSetCost = 0;
            for (ElementRune element : actionNode.elements) {
                if(!level.isClientSide()) {
                    for (int i = 0; i < intensity; i++) {
                        //TODO: reimplement server/client target sync
                        BlockPos targetBlock = form.getTargetBlock();

                        if (targetBlock != null) {
                            elementSetCost += element.proposeCostForBlock(level, form, targetBlock, actionNode.action, this);
                        }
                    }
                }

                elementSetCost += element.proposeCostForIntensityChange(actionNode.action, this);
                intensity = element.updateIntensity(actionNode.action, intensity);

                List<Entity> selectedEntities = form.getTargetEntities();
                for (Entity e : selectedEntities) {
                    elementSetCost += element.proposeCostForEntity(level, e, actionNode.action, this);
                }
            }
            actionCostSum += actionNode.action.applyEfficiencyToCost(elementSetCost);
        }

        return this.form.applyEfficiencyToCost(actionCostSum);
    }

    public void cast(Level level) {

        intensity = RuneSequence.BASE_INTENSITY;
        for(ActionNode actionNode : actions) {
            for(ElementRune element : actionNode.elements) {

                if(!level.isClientSide()) {
                    for (int i = 0; i < intensity; i++) {
                        BlockPos targetBlock = form.getTargetBlock();
                        if (targetBlock != null) {
                            element.applyActionOnBlock(level, form, targetBlock, actionNode.action, this);
                        }
                    }
                } else {
                    BlockPos targetBlock = form.getTargetBlock();
                    if(targetBlock != null) {
                        element.createParticle(level, targetBlock, actionNode.action);
                    }
                }

                element.applyActionOnVolume(level, form, actionNode.action, this);
                intensity = element.updateIntensity(actionNode.action, intensity);

                List<Entity> selectedEntities = form.getTargetEntities();
                if(!level.isClientSide() || element.canRunClientSide()) {
                    for (Entity e : selectedEntities) {
                        element.applyActionOnEntity(level, e, actionNode.action, this);
                    }
                }
            }
        }
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

    public String formName() {
        if(form != null) {
            return form.name();
        } else {
            return "";
        }
    }

    public boolean isCastable() {
        return form != null && !actions.isEmpty() && !actions.getFirst().elements.isEmpty();
    }
}
