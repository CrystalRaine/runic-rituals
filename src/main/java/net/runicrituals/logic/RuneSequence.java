package net.runicrituals.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.ManaStorage;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.logic.runes.logic.ModifierRune;
import net.runicrituals.registries.components.RuneDataComponent;

import java.util.ArrayList;
import java.util.List;

public class RuneSequence {

    public static final int BASE_INTENSITY = 1;
    public static final int BASE_FORM_RADIUS = 4;

    List<CastingBlock> castingBlocks = new ArrayList<>();

    public void run(Level level, ManaStorage mana) {

        for(CastingBlock castingBlock : this.castingBlocks) {
            if(castingBlock.isUncastable()) continue;

            castingBlock.applyModifiers();
            double cost;

//            if(!level.isClientSide()) {
//                cost = castingBlock.proposeManaCost(level);
//            } else {
                cost = 0;
//            }

            if(mana.applyManaValue(cost)) {
                castingBlock.cast(level);
            }
        }
    }

    public void createCastingBlocks(Level level, List<RuneDataComponent> runeData, BlockPos ritualPosition) {

        List<Rune> runes = runeData
                .stream()
                .map(rd -> Rune.create(RuneSymbol.getSymbolFromId(rd.runeSymbol()), RuneInlayMaterial.getElementFromId(rd.inlay())))
                .toList();

        castingBlocks = new ArrayList<>();
        List<ModifierRune> modifierRuneBuffer = new ArrayList<>();

        for(Rune r : runes) {
            switch (r.getType()) {
                case ACTION -> {
                    if(castingBlocks.isEmpty()) continue;
                    castingBlocks.getLast().addAction((ActionRune) r);
                }
                case FORM -> {
                    castingBlocks.add(new CastingBlock((FormRune) r));
                    castingBlocks.getLast().setFormModifiers(modifierRuneBuffer);
                    modifierRuneBuffer = new ArrayList<>();
                    ((FormRune) r).setProperties(level, ritualPosition, BASE_FORM_RADIUS);
                }
                case FORM_MODIFIER -> {
                    modifierRuneBuffer.add((ModifierRune) r);
                }
                case ELEMENT -> {
                    if(castingBlocks.isEmpty()) continue;
                    castingBlocks.getLast().addElement((ElementRune) r);
                }
                default -> {}
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
