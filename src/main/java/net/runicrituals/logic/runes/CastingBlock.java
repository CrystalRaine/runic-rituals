package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CastingBlock {

    private static class ActionNode {
        private ActionRune action;

        private final List<ElementRune> elements = new ArrayList<>();

    }

    FormRune form;
    private final List<ActionNode> actions = new ArrayList<>();

    public CastingBlock(FormRune form) {
        this.form = form;
    }

    private final DequeWrapper deque = new DequeWrapper();
    private static class DequeWrapper {
        private final ArrayList<BlockPos> blockPosDeque = new ArrayList<>();
        private int index = 0;
        private boolean writing = true;

        /**
         * needed for this, hence the wrapper class
         * @return BlockPos
         */
        protected BlockPos peakNext() {
            if(index >= blockPosDeque.size()) {
                throw new ArrayIndexOutOfBoundsException("Trying to read past end of deque. may have forgotten to clearIndex() in writing");
            }
//            note that index is used BEFORE increment
            return blockPosDeque.get(index++);
        }

        protected void stopWriting() {
            writing = false;
        }

        protected BlockPos enqueue(FormRune form, Level level, Position pos) {
            if(!writing) {
               return peakNext();
            } else {
                BlockPos targetPos = form.getTargetBlock(level, pos);
                blockPosDeque.add(targetPos);
                return blockPosDeque.getLast();
            }
        }

        protected BlockPos dequeue() {
            return blockPosDeque.removeFirst();
        }

        protected void clearIndex() {
            index = 0;
        }

        protected void reset() {
            if(!blockPosDeque.isEmpty()) {
                throw new ArrayIndexOutOfBoundsException("Did not empty deque before calling reset(). may have mismatch between enqueue calls and dequeue calls");
            }

            clearIndex();
            blockPosDeque.clear();
        }

        protected boolean isEmpty() {
            return blockPosDeque.isEmpty();
        }

    }


    public double proposeManaCost(Level level, Position initialPos, RuneSequence sequence) {

        if(!this.isCastable()) return Double.POSITIVE_INFINITY;

        sequence.resetIntensity();
        double actionCostSum = 0;
        for(ActionNode actionNode : actions) {
            double elementSetCost = 0;
            for (ElementRune element : actionNode.elements) {
                List<Entity> selectedEntities = form.getTargetEntities(level, initialPos);
                sequence.intensity = element.updateIntensity(actionNode.action, sequence.intensity);

                if(!level.isClientSide()) {

                    elementSetCost += element.proposeCost(actionNode.action, sequence);

                    for (int i = 0; i < sequence.intensity; i++) {
                        BlockPos targetBlock = deque.enqueue(form, level, initialPos);

                        if(targetBlock != null) {
                            elementSetCost += element.proposeCost(level, targetBlock, actionNode.action, sequence);
                        }
                    }

                    for (Entity e : selectedEntities) {
                        elementSetCost += element.proposeCost(level, e, actionNode.action, sequence);
                    }
                }
            }
            actionCostSum += actionNode.action.applyEfficiencyToCost(elementSetCost);
        }

//        clear the index, and stop writing for if mana cost is called again.
        deque.clearIndex();
        deque.stopWriting();
        sequence.resetIntensity();

        return this.form.applyEfficiencyToCost(actionCostSum);
    }

    public void resetQueue() {
        deque.reset();
    }

    public void cast(Level level, Position initialPos, RuneSequence sequence) {

        sequence.resetIntensity();
        for(ActionNode actionNode : actions) {
            for(ElementRune element : actionNode.elements) {
                List<Entity> selectedEntities = form.getTargetEntities(level, initialPos);

                sequence.intensity = element.updateIntensity(actionNode.action, sequence.intensity);

                if(!level.isClientSide()) {

                    for (int i = 0; i < sequence.intensity; i++) {
                        BlockPos targetBlock = deque.dequeue();
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

        sequence.resetIntensity();
        deque.reset();
    }

    public double getManaCost(Level level, Position initialPos, RuneSequence sequence) {
        return proposeManaCost(level, initialPos, sequence);
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
