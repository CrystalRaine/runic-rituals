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

    public static void run(Level level, ManaStorage mana, List<RuneDataComponent> runeData, List<BlockPos> areaBase, BlockPos min, BlockPos max) {

        List<Rune> runes = runeData.stream().map(rd -> Rune.create(RuneSymbol.getSymbolFromId(rd.runeSymbol()), RuneInlayMaterial.getElementFromId(rd.inlay()))).toList();
        List<CastingBlock> castingBlocks = createCastingBlocks(runes, level, areaBase, min, max);

        for(CastingBlock castingBlock : castingBlocks) {
            if(!castingBlock.isCastable()) continue;

            castingBlock.applyModifiers();
            double cost;

            if(!level.isClientSide()) {
                cost = castingBlock.proposeManaCost(level);
            } else {
                cost = Double.POSITIVE_INFINITY;
            }

//            if(!level.isClientSide()) {
//                RunicRituals.LOGGER.info("Cost/Available: {}/{}/{}", (int) cost, (int)mana.getMana(), mana.applyManaValue((int) cost));
//            }
            if(mana.applyManaValue((int) cost)) {
                castingBlock.cast(level);
            }
        }
    }

    public static List<CastingBlock> createCastingBlocks(List<Rune> runes, Level level, List<BlockPos> base, BlockPos min, BlockPos max) {
        List<CastingBlock> castingBlocks = new ArrayList<>();
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
                    ((FormRune) r).setProperties(level, base, min, max);
                }
                case MODIFIER -> {
                    modifierRuneBuffer.add((ModifierRune) r);
                }
                case ELEMENT -> {
                    if(castingBlocks.isEmpty()) continue;
                    castingBlocks.getLast().addElement((ElementRune) r);
                }
            }
        }

        return castingBlocks;
    }
}
