package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneMaterial;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RunicRitualsTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public static final TagKey<Item> INLAYABLE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "inlay_items"));
    public static final TagKey<Item> ENGRAVABLE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "engrave_items"));

    public RunicRitualsTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        for(RuneMaterial material : RuneMaterial.values()) {
            Optional<ResourceKey<Item>> key = BuiltInRegistries.ITEM.getResourceKey(material.getAssociatedItem());
            key.ifPresent(itemResourceKey -> builder(INLAYABLE_ITEMS).add(itemResourceKey));
        }

        builder(ENGRAVABLE_ITEMS)
                .add(RunicRitualsItems.RUNESTONE_KEY)
                .add(RunicRitualsBlocks.RUNESLATE_KEY)
        ;
    }
}