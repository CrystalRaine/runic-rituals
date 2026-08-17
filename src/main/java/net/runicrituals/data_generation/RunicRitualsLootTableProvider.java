package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.runicrituals.registries.RunicRitualsBlocks;

import java.util.concurrent.CompletableFuture;

public class RunicRitualsLootTableProvider extends FabricBlockLootSubProvider {
    public RunicRitualsLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(RunicRitualsBlocks.RUNESLATE);
        dropSelf(RunicRitualsBlocks.RUNE_ENGRAVER);
        super.add(RunicRitualsBlocks.RUNE_OBELISK, createDoorTable(RunicRitualsBlocks.RUNE_OBELISK));
    }
}