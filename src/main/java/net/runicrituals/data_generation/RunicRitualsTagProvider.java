package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RunicRitualsTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public RunicRitualsTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        for(RuneInlayMaterial material : RuneInlayMaterial.values()) {
            Optional<ResourceKey<Item>> key = BuiltInRegistries.ITEM.getResourceKey(material.getAssociatedItem());
            key.ifPresent(itemResourceKey -> builder(RuneEngravingRecipe.INLAYABLE_ITEMS).add(itemResourceKey));
        }

        builder(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                .add(RunicRitualsItems.RUNESTONE_KEY)
                .add(RunicRitualsBlocks.RUNESLATE_KEY)
        ;
    }
}