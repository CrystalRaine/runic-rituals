package net.runicrituals.logic.runes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.ManaStorageHandler;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.modifier.ModifierRune;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;

import java.util.*;

public class RuneSequence {

    public static final int BASE_INTENSITY = 1;
    public static final int BASE_FORM_RADIUS = 4;

    List<CastingBlock> castingBlocks = new ArrayList<>();
    List<CastingBlockTrigger> pendingTriggerBlocks = new ArrayList<>();

    public void tryCastCastingBlock(CastingBlock castingBlock, BlockPos ritualPosition, ManaStorageHandler mana, Level level) {
        if(castingBlock.isUncastable()) return;

        castingBlock.setLocation(ritualPosition);

        CastingBlockTrigger potentialTrigger = castingBlock.resolveModifiers();

        if(potentialTrigger != null) {
            // check if this exact trigger block already exists, and if so, don't add it
            // alternatively, if the trigger is allowed to create multiple, add anyway
            if(!potentialTrigger.triggerType.allowedToCreateTrigger() ||
                    (!potentialTrigger.triggerType.allowedMultiTrigger() &&
                            pendingTriggerBlocks.stream().anyMatch(t-> t.hash == potentialTrigger.hash)
                    )
            ) return;
            pendingTriggerBlocks.add(potentialTrigger);
            return;
        }

        double cost = 0;
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
        List<ModifierRune> blockModifierRunes = new ArrayList<>();

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
                    buildingBlock.setModifierRunes(blockModifierRunes);

                    ((FormRune) r).setProperties(level, BASE_FORM_RADIUS);
                }
                case MODIFIER -> blockModifierRunes.add((ModifierRune)r);
                default -> {}
            }
        }

        if(buildingBlock != null){
            castingBlocks.add(buildingBlock);
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

    public void triggeredCastingBlock(CastTrigger castTrigger, BlockPos triggeringRSEPos, ManaStorageHandler mana, Level level) {
        // new pendingTriggerBlock entries can be added as we go through these, but shouldn't ever be removed during execution.
        // additionally, we don't care about trying to process those new blocks (for a couple of reasons really),
        // so we snapshot the filtered ones we should process, then process those
        Optional<CastingBlockTrigger> cbt = pendingTriggerBlocks.stream().filter(b -> b.triggerType == castTrigger && b.triggerRSEPos.equals(triggeringRSEPos)).findFirst();

        if(cbt.isEmpty()) return;
        tryCastCastingBlock(cbt.get().castOnTrigger(), cbt.get().castOnTrigger.getForm().getPosition(), mana, level);
        pendingTriggerBlocks.remove(cbt.get());
    }

    public record CastingBlockTrigger(CastTrigger triggerType, BlockPos triggerRSEPos, int hash, CastingBlock castOnTrigger){}

}
