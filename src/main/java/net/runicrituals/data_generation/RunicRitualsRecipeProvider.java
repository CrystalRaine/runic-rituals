package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.runicrituals.data_generation.recipe_builders.RuneEngravingRecipeBuilder;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class RunicRitualsRecipeProvider extends FabricRecipeProvider {

    public RunicRitualsRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                shaped(RecipeCategory.MISC, RunicRitualsItems.BASIC_WAND)
                        .pattern(" #o")
                        .pattern(" /#")
                        .pattern("#  ")
                        .define('#', Items.COPPER_INGOT)
                        .define('/', Items.STICK)
                        .define('o', Items.AMETHYST_SHARD)
                        .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                        .save(output);

                shaped(RecipeCategory.MISC, RunicRitualsBlocks.RUNE_ENGRAVER)
                        .pattern(" o/")
                        .pattern("~~~")
                        .pattern("~ ~")
                        .define('~', Items.IRON_INGOT)
                        .define('/', Items.STICK)
                        .define('o', Items.AMETHYST_SHARD)
                        .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                        .save(output);

                shaped(RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE, 3)
                        .pattern("###")
                        .define('#', RunicRitualsItems.RUNESTONE)
                        .unlockedBy(getHasName(RunicRitualsItems.RUNESTONE), has(RunicRitualsItems.RUNESTONE))
                        .save(output);

                shapeless(RecipeCategory.MISC, RunicRitualsItems.RUNESTONE, 8)
                        .requires(Items.POLISHED_DEEPSLATE)
                        .unlockedBy(getHasName(Items.POLISHED_DEEPSLATE), has(Items.POLISHED_DEEPSLATE))
                        .save(output);

                for(RuneSymbol symbol : RuneSymbol.values() ) {
                    RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, symbol.getSymbolItem())
                            .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                            .inlayMaterial(RuneEngravingRecipe.INLAYABLE_ITEMS)
                            .unlockedBy(getHasName(RunicRitualsItems.RUNESTONE), has(RunicRitualsItems.RUNESTONE))
                            .save(output);

                    RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, symbol.getSymbolItem())
                            .setIdAffix("etched")
                            .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                            .unlockedBy(getHasName(RunicRitualsItems.RUNESTONE), has(RunicRitualsItems.RUNESTONE))
                            .save(output);
                }
            }
        };
    }

    @Override
    public String getName() {
        return "RunicRitualsRecipeProvider";
    }
}
