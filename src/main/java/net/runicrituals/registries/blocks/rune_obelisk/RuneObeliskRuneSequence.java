package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.CastingBlock;
import net.runicrituals.logic.runes.Rune;
import net.runicrituals.logic.RuneSequence;

import java.util.ArrayList;
import java.util.List;

public class RuneObeliskRuneSequence {

    SingleBlockRitualEntity triggeringRitual;
    Level level;
    Position initialPos;

    List<Rune> runes = new ArrayList<>();
    List<CastingBlock> castingBlocks = null;

    public RuneObeliskRuneSequence(SingleBlockRitualEntity triggeringRitual, Level level, Position initialPos) {
        this.triggeringRitual = triggeringRitual;
        this.level = level;
        this.initialPos = initialPos;
    }

    public void tick() {

//        if(castingBlocks == null) {
//            castingBlocks = RuneSequence.createCastingBlocks(runes);
//        }
//
//        if(castingBlocks.isEmpty()) return;
//
//        int blockId = 0;
//        for(CastingBlock block : castingBlocks) {
//            if(block.isCastable()) {
//                block.applyModifiers();
//                double cost;
//
//                if(level.isClientSide() && triggeringRitual.getCachedCostForBlock(blockId) != null) {
//                    cost = triggeringRitual.getCachedCostForBlock(blockId);
//                } else if (!level.isClientSide()){
//                    cost = block.proposeManaCost(level, initialPos);
//                    if(null == triggeringRitual.getCachedCostForBlock(blockId) || cost != triggeringRitual.getCachedCostForBlock(blockId)) { // if block cost has updated
//                        triggeringRitual.cacheBlockCost(blockId, cost);
//                    }
//                } else {
//                    cost = Double.POSITIVE_INFINITY;
//                }
//
//                blockId ++;
//                double proposedPostCastMana = triggeringRitual.getMana() - cost;
//
//                if(proposedPostCastMana <= triggeringRitual.getManaCap() && proposedPostCastMana >= 0) {
//                    block.cast(level, initialPos);
//                    triggeringRitual.addMana(-cost);
//                    block.resetQueue();
//                }
//            }
//        }
//
//        // update client if a block's cost changed.
//        if(triggeringRitual.isBlockCostCacheDirty())
//            triggeringRitual.setChanged();
    }

    /**
     * return the aggregate mana cost of all casting blocks
     * @return mana cost
     */
    public double getManaCost() {
//        castingBlocks = RuneSequence.createCastingBlocks(runes);
//
//        double cost = 0;
//        for(CastingBlock block : castingBlocks) {
//            if(block.isCastable()) {
//                cost += block.proposeManaCost(level, initialPos);
//            }
//        }
//        return cost;
        return 0;
    }

    public void clearRunes() {
        runes.clear();
        castingBlocks = null;
    }

    public void addRune(RuneSymbol symbol, RuneInlayMaterial inlay) {
        runes.add(Rune.create(symbol, inlay));
    }
}
