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

import java.util.Locale;
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
                        .pattern("##")
                        .define('#', Items.COBBLED_DEEPSLATE)
                        .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE))
                        .save(output);

                for(RuneSymbol symbol : RuneSymbol.values() ) {
                    String symbolName = symbol.getName().toLowerCase(Locale.ROOT).replace(" ", "_");
                    RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE.asItem())
                            .setOutputSymbol(symbol)
                            .setIdAffix("inlayed_" + symbolName)
                            .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                            .inlayMaterial(RuneEngravingRecipe.INLAYABLE_ITEMS)
                            .unlockedBy(getHasName(RunicRitualsBlocks.RUNESLATE), has(RunicRitualsBlocks.RUNESLATE))
                            .save(output);

                    RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE.asItem())
                            .setOutputSymbol(symbol)
                            .setIdAffix("etched_" + symbolName)
                            .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                            .unlockedBy(getHasName(RunicRitualsBlocks.RUNESLATE), has(RunicRitualsBlocks.RUNESLATE))
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
