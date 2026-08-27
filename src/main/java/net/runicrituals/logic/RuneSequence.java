package net.runicrituals.logic;

import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
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

    private static final int BASE_INTENSITY = 1;
    public double intensity = BASE_INTENSITY;

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
    }

    public void tick() {

        if(castingBlocks == null) {
            buildCastingBlocks();
        }

        if(castingBlocks.isEmpty()) return;

        int blockId = 0;
        for(CastingBlock block : castingBlocks) {
            if(block.isCastable()) {
                double cost;

                if(level.isClientSide() && triggeringRitual.getCachedCostForBlock(blockId) != null) {
                    cost = triggeringRitual.getCachedCostForBlock(blockId);
                } else if (!level.isClientSide()){
                    cost = block.getManaCost(level, initialPos, this);
                    if(null == triggeringRitual.getCachedCostForBlock(blockId) || cost != triggeringRitual.getCachedCostForBlock(blockId)) { // if block cost has updated
                        triggeringRitual.cacheBlockCost(blockId, cost);
                    }
                } else {
                    cost = Double.POSITIVE_INFINITY;
                }

                blockId ++;
                double proposedPostCastMana = triggeringRitual.getMana() - cost;

                if(proposedPostCastMana <= triggeringRitual.getManaCap() && proposedPostCastMana >= 0) {
                    block.cast(level, initialPos, this);
                    triggeringRitual.addMana(-cost);
                    block.resetQueue();
                }
            }
        }

//      update client if a block's cost changed.
        if(triggeringRitual.isBlockCostCacheDirty())
            triggeringRitual.setChanged();
    }

    /**
     * return the aggregate mana cost of all casting blocks
     * @return mana cost
     */
    public double getManaCost() {
        buildCastingBlocks();

        double cost = 0;
        for(CastingBlock block : castingBlocks) {
            if(block.isCastable()) {
                cost += block.getManaCost(level, initialPos, this);
            }
        }
        return cost;
    }

    public void clearRunes() {
        runes.clear();
        castingBlocks = null;
        intensity = BASE_INTENSITY;
    }

    public void addRune(RuneSymbol symbol, RuneInlayMaterial inlay) {
        runes.add(Rune.create(symbol, inlay));
    }

    public void resetIntensity() {
        intensity = BASE_INTENSITY;
    }
}
