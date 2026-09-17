package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.enums.RuneInlayMaterial;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RunicRitualsItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public static final TagKey<Item> INLAYABLE_ITEMS =  TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "inlay_items"));
    public static final TagKey<Item> ENGRAVABLE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "engrave_items"));
    public static final TagKey<Item> WANDS =            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "wands"));

    List<ResourceKey<Item>> wands = List.of(
            RunicRitualsItems.BASIC_WAND_KEY,
            RunicRitualsItems.DIAMOND_WAND_KEY,
            RunicRitualsItems.ECHO_WAND_KEY
    );

    public RunicRitualsItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {

        // grab all items from the inlay materials enum instead of listing all out here. may need to reverse that relationship later.
        for(RuneInlayMaterial material : RuneInlayMaterial.values()) {
            Optional<ResourceKey<Item>> key = BuiltInRegistries.ITEM.getResourceKey(material.getAssociatedItem());
            key.ifPresent(itemResourceKey -> builder(INLAYABLE_ITEMS).add(itemResourceKey));
        }

        builder(ENGRAVABLE_ITEMS)
                .add(RunicRitualsBlocks.RUNESLATE_KEY)
        ;

        for(ResourceKey<Item> wand : wands) {
            builder(WANDS).add(wand);
            builder(ItemTags.MINING_ENCHANTABLE).add(wand);
            builder(ItemTags.DURABILITY_ENCHANTABLE).add(wand);
        }
    }
}