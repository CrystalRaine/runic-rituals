package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;
import net.runicrituals.registries.items.RunicRitualsItem;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RunicRitualsBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public static final TagKey<Block> MINEABLE_WITH_WAND = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "wand"));

    public RunicRitualsBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {

        builder(MINEABLE_WITH_WAND)
                .add(RunicRitualsBlocks.RUNESLATE_KEY)
                .add(RunicRitualsBlocks.RITUAL_ANCHOR_KEY)
                .add(RunicRitualsBlocks.RUNE_ENGRAVER_KEY)
        ;
    }
}