package net.runicrituals.logic;

import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.runes.action.ActionRune;
import net.runicrituals.logic.runes.element.ElementRune;
import net.runicrituals.logic.runes.form.FormRune;
import net.runicrituals.registries.blocks.RitualEntity;

import java.util.ArrayList;
import java.util.List;

public class RuneSequence {

    RitualEntity triggeringRitual;
    Level level;
    Position initialPos;

    List<Rune> runes = new ArrayList<>();
    List<CastingBlock> castingBlocks = null;

    public static final int BASE_SEQUENCE_COST = 10;
    public int intensity = 1;

    public RuneSequence(RitualEntity triggeringRitual, Level level, Position initialPos) {
        this.triggeringRitual = triggeringRitual;
        this.level = level;
        this.initialPos = initialPos;
    }

    public void buildCastingBlocks() {
        List<CastingBlock> blocks = new ArrayList<>();

        for(Rune r : runes) {
            switch (r.getType()) {
                case ACTION -> {
                    if(blocks.isEmpty()) continue;
                    blocks.getLast().addAction((ActionRune) r);
                }
                case FORM -> {
                    blocks.add(new CastingBlock((FormRune) r));
                }
                case LOGIC -> {
//                    Do nothing for now
                }
                case ELEMENT -> {
                    if(blocks.isEmpty()) continue;
                    blocks.getLast().addElement((ElementRune) r);
                }
            }
        }

        this.castingBlocks = blocks;

        for(CastingBlock block : blocks) {
            block.getIntensity(this);
        }
    }

    public void tick() {

        if(castingBlocks == null) {
            buildCastingBlocks();
        }

        if(castingBlocks.isEmpty()) return;

        for(CastingBlock block : castingBlocks) {
            if(block.isCastable()) {
                block.cast(level, initialPos, this);
            }
        }
        this.triggeringRitual.addMana(-getManaCost()); // pay cost
    }

    public double getManaCost() {
        buildCastingBlocks();

        double cost = 0;
        for(CastingBlock block : castingBlocks) {
            if(block.isCastable()) {
                cost += block.getManaCost();
            }
        }
        return cost;
    }

    public void clearRunes() {
        runes.clear();
        castingBlocks = null;
        intensity = 1;
    }

    public void addRune(RuneSymbol symbol, RuneInlayMaterial inlay) {
        runes.add(Rune.create(symbol, inlay));
    }
}
