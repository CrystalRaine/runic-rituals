package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.form_modifier.ModifierRune;
import net.runicrituals.logic.runes.position_modifier.PositionModifierRune;

import java.util.ArrayList;
import java.util.List;

public class CastingBlock {

    private List<ModifierRune> formModifiers = new ArrayList<>();
    private List<PositionModifierRune> positionModifiers = new ArrayList<>();
    private final FormRune form;
    private final List<ActionNode> actions = new ArrayList<>();
    public double intensity = 0;

    private final List<BlockPos> targets = new ArrayList<>();
    private int cursorPos = 0;
    private void resetCursor() {
        cursorPos = 0;
    }
    private void resetIntensity() {
        intensity = RuneSequence.BASE_INTENSITY;
    }

    private BlockPos getTarget(FormRune form) {
        if(cursorPos >= targets.size()) {
            BlockPos targetBlock = form.getBlockTarget();
            targets.add(targetBlock);
            return targetBlock;
        } else {
            return targets.get(cursorPos);
        }
    }
    
    private void resetTargets(){
        targets.clear();
    }

    public int runeCount() {
        if(isUncastable()) return 0;
        int count = 0;

        count += (form == null ? 0 : 1); // form exists
        count += formModifiers.size();   // modifier count

        for(ActionNode action : actions) { // count up actions and elements
            count += action.count();
        }

        return count;
    }

    public void setLocation(BlockPos actionLocation) {
        form.setPosition(actionLocation);
    }

    private static class ActionNode {
        private ActionRune action;
        private final List<ElementRune> elements = new ArrayList<>();

        public int count() {
            int count = 0;
            count += action == null ? 0 : 1;
            count += elements.size();
            return count;
        }
    }

    public CastingBlock(FormRune form) {
        this.form = form;
    }

    public void setFormModifiers(List<ModifierRune> formModifiers) {
        this.formModifiers = formModifiers;
    }
    public void setPositionModifiers(List<PositionModifierRune> positionModifiers) {
        this.positionModifiers = positionModifiers;
    }

    public void applyModifiers(BlockPos overridePos) {
        applyFormModifiers();
        applyPositionModifiers(overridePos);
    }

    private void applyPositionModifiers(BlockPos pos) {
        if(pos == null) return;

        for(PositionModifierRune pmr : positionModifiers) {
            BlockPos updatePos = pmr.getOverridePosition(pos);
            if(updatePos == null) return;
            setLocation(updatePos);
        }
    }

    private void applyFormModifiers() {
        form.setDefaultRadius();
        for(ModifierRune modifier : formModifiers) {
            modifier.applyModification(form);
        }
    }

    public double proposeManaCost(Level level) {
        if(this.isUncastable()) return Double.POSITIVE_INFINITY;

        double actionCostSum = 0;
        resetIntensity();
        resetCursor();

        for(ActionNode actionNode : actions) {
            double elementSetCost = 0;
            for (ElementRune element : actionNode.elements) {
                if(!level.isClientSide()) {
                    for (int i = 0; i < intensity; i++) {
                        BlockPos targetBlock = getTarget(form);

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

        resetIntensity();
        resetCursor();

        // use reversed lists to keep consistent-clockwise order
        for(ActionNode actionNode : actions.reversed()) {
            for(ElementRune element : actionNode.elements.reversed()) {

                if(!level.isClientSide()) {
                    for (int i = 0; i < intensity; i++) {
                        BlockPos targetBlock = form.getBlockTarget();
                        if (targetBlock != null) {
                            element.applyActionOnBlock(level, form, targetBlock, actionNode.action, this);
                        }
                    }
                } else {
                    BlockPos targetBlock = getTarget(form);
                    element.createParticle(level, targetBlock, actionNode.action);
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
        resetTargets();
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
            return form.toString();
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(formName() + ": ");
        for(ActionNode an : actions.reversed()) {
            s.append(an.action.toString()).append(" [ ");
            for(ElementRune e : an.elements.reversed()) {
                s.append(e.toString()).append(" ");
            }
            s.append("]");
        }

        return s.toString();

    }

    public boolean isUncastable() {
        return form == null || actions.isEmpty() || actions.getFirst().elements.isEmpty();
    }
}
