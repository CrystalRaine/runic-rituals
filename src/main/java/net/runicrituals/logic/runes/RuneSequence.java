package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.ManaStorageHandler;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.condition_modifier.ConditionModifierRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.enums.CastTriggers;
import net.runicrituals.logic.runes.enums.RuneInlayMaterial;
import net.runicrituals.logic.runes.enums.RuneSymbol;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.form_modifier.ModifierRune;
import net.runicrituals.logic.runes.position_modifier.PositionModifierRune;
import net.runicrituals.registries.components.RuneDataComponent;

import java.util.*;

public class RuneSequence {

    public static final int BASE_INTENSITY = 1;
    public static final int BASE_FORM_RADIUS = 4;

    List<CastingBlock> castingBlocks = new ArrayList<>();
    Dictionary<CastTriggers, List<CastingBlock>> triggerCastingBlocks = new Hashtable<>();

    BlockPos overridePos = null;

    public void setBoundLocation(BlockPos position) {
        overridePos = position;
    }

    public void castTriggeredBlocks(Level level, ManaStorageHandler mana, CastTriggers triggerType, BlockPos location) {
        List<CastingBlock> triggeredCastingBlocks = triggerCastingBlocks.get(triggerType);
        if(triggeredCastingBlocks == null) return;

        for(CastingBlock castingBlock : triggeredCastingBlocks) {
            tryCastCastingBlock(castingBlock, location, mana, level);
        }
    }

    public void tryCastCastingBlock(CastingBlock castingBlock, BlockPos ritualPosition, ManaStorageHandler mana, Level level) {
        if(castingBlock.isUncastable()) return;

        castingBlock.setLocation(ritualPosition);
        castingBlock.applyModifiers(overridePos);
        double cost;

        cost = 0; //TODO: reimplement costs

        if(mana.applyManaValue(cost)) {
            castingBlock.cast(level);
        }
    }

    public void run(Level level, ManaStorageHandler mana, BlockPos ritualPosition) {
        for(CastingBlock castingBlock : this.castingBlocks) {
            tryCastCastingBlock(castingBlock, ritualPosition, mana, level);
        }
    }

    public void createCastingBlocks(Level level, List<RuneDataComponent> runeData) {

        List<Rune> runes = runeData
                .stream()
                .map(rd -> Rune.create(RuneSymbol.getSymbolFromId(rd.runeSymbol()), RuneInlayMaterial.getElementFromId(rd.inlay())))
                .toList();

        castingBlocks = new ArrayList<>();
        triggerCastingBlocks = new Hashtable<>();

        CastingBlock buildingBlock = null;
        List<ModifierRune> modifierRuneBuffer = new ArrayList<>();
        List<PositionModifierRune> positionModifierRunes = new ArrayList<>();
        CastTriggers trigger = null;

        for(Rune r : runes) {
            switch (r.getType()) {
                case ELEMENT -> {
                    if(buildingBlock == null) continue;
                    buildingBlock.addElement((ElementRune) r);
                }
                case ACTION -> {
                    if(buildingBlock == null) continue;
                    buildingBlock.addAction((ActionRune) r);
                }
                case FORM -> {
                    if(buildingBlock != null){
                        if(trigger == null) {
                            castingBlocks.add(buildingBlock);
                        } else {
                            if(triggerCastingBlocks.get(trigger) == null) {
                                triggerCastingBlocks.put(trigger, new ArrayList<>());
                            }
                            triggerCastingBlocks.get(trigger).add(buildingBlock);
                        }
                    }

                    buildingBlock = new CastingBlock((FormRune) r);
                    buildingBlock.setFormModifiers(modifierRuneBuffer);
                    buildingBlock.setPositionModifiers(positionModifierRunes);

                    modifierRuneBuffer = new ArrayList<>();

                    ((FormRune) r).setProperties(level, BASE_FORM_RADIUS);
                }
                case CONDITION_MODIFIER -> {
                    if(buildingBlock != null){
                        if(trigger == null) {
                            castingBlocks.add(buildingBlock);
                        } else {
                            if(triggerCastingBlocks.get(trigger) == null) {
                                triggerCastingBlocks.put(trigger, new ArrayList<>());
                            }
                            triggerCastingBlocks.get(trigger).add(buildingBlock);
                        }
                        buildingBlock = null;
                    }

                    trigger = ((ConditionModifierRune) r).getCastTrigger();
                }
                case FORM_MODIFIER -> {
                    modifierRuneBuffer.add((ModifierRune) r);
                }
                case POSITION_MODIFIER -> {
                    positionModifierRunes.add((PositionModifierRune) r);
                }
                default -> {}
            }
        }

        if(buildingBlock != null){
            if(trigger == null) {
                castingBlocks.add(buildingBlock);
            } else {
                if(triggerCastingBlocks.get(trigger) == null) {
                    triggerCastingBlocks.put(trigger, new ArrayList<>());
                }
                triggerCastingBlocks.get(trigger).add(buildingBlock);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder r = new StringBuilder();
        for(CastingBlock b : castingBlocks){
            r.append(b);
        }

        return r.toString();
    }

    public int size() {

        int count = 0;

        for(CastingBlock block : castingBlocks) {
            if(block.isUncastable()) continue;
            count += block.runeCount();
        }

        return count;
    }
}
