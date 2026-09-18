package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.ManaStorageHandler;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.condition_modifier.ConditionModifierRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.enums.CastTriggers;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.form_modifier.FormModifierRune;
import net.runicrituals.logic.runes.position_modifier.PositionModifierRune;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;

import java.util.*;

public class RuneSequence {

    public static final int BASE_INTENSITY = 1;
    public static final int BASE_FORM_RADIUS = 4;

    List<CastingBlock> castingBlocks = new ArrayList<>();

    public void tryCastCastingBlock(CastingBlock castingBlock, BlockPos ritualPosition, ManaStorageHandler mana, Level level) {
        if(castingBlock.isUncastable()) return;

        castingBlock.setLocation(ritualPosition);

        boolean condition = castingBlock.applyModifiers();
        if(!condition) return;
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

    public void createCastingBlocks(Level level, List<RuneslateEntity> runeData) {

        List<Rune> runes = runeData
                .stream()
                .map(Rune::create)
                .toList();

        castingBlocks = new ArrayList<>();

        CastingBlock buildingBlock = null;
        List<FormModifierRune> formModifierRunes = new ArrayList<>();
        List<PositionModifierRune> positionModifierRunes = new ArrayList<>();
        List<ConditionModifierRune> conditionModifierRunes = new ArrayList<>();
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
                        castingBlocks.add(buildingBlock);
                    }

                    buildingBlock = new CastingBlock((FormRune) r);
                    buildingBlock.setFormModifiers(formModifierRunes);
                    buildingBlock.setPositionModifiers(positionModifierRunes);
                    buildingBlock.setConditionModifiers(conditionModifierRunes);

                    formModifierRunes = new ArrayList<>();

                    ((FormRune) r).setProperties(level, BASE_FORM_RADIUS);
                }
                case CONDITION_MODIFIER -> conditionModifierRunes.add((ConditionModifierRune) r);
                case FORM_MODIFIER -> formModifierRunes.add((FormModifierRune) r);
                case POSITION_MODIFIER -> positionModifierRunes.add((PositionModifierRune) r);
                default -> {}
            }
        }

        if(buildingBlock != null){
            if(trigger == null) {
                castingBlocks.add(buildingBlock);
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
