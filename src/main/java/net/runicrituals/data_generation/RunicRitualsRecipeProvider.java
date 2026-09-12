package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.AllIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.runicrituals.data_generation.recipe_builders.RuneEngravingRecipeBuilder;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeProvider.getHasName;

public class RunicRitualsRecipeProvider extends FabricRecipeProvider {

    public RunicRitualsRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {

                Ingredient deepslateIngredients = Ingredient.of(
                    Items.DEEPSLATE,
                    Items.COBBLED_DEEPSLATE,
                    Items.DEEPSLATE_BRICKS,
                    Items.DEEPSLATE_TILES,
                    Items.POLISHED_DEEPSLATE,
                    Items.CRACKED_DEEPSLATE_BRICKS,
                    Items.CRACKED_DEEPSLATE_TILES,
                    Items.CHISELED_DEEPSLATE
                );

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
                    .define('#', deepslateIngredients)
                    .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE))
                    .save(output);

                for(RuneSymbol symbol : RuneSymbol.values() ) {

                    createInlayRecipe(symbol, registries, this, output);
                    createEtchRecipe(symbol, registries, this, output);

                }
            }
        };
    }

    void createEtchRecipe(RuneSymbol symbol, HolderLookup.Provider registries, RecipeProvider recipeProvider, RecipeOutput output) {

        if(!symbol.getMaterialsAllowed().contains(RuneInlayMaterial.ETCHED) && !symbol.getMaterialsAllowed().isEmpty()) return;

        String symbolName = symbol.getName().toLowerCase(Locale.ROOT).replace(" ", "_");
        RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE.asItem())
            .setOutputSymbol(symbol)
            .setIdAffix("etched_" + symbolName)
            .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
            .unlockedBy(getHasName(RunicRitualsBlocks.RUNESLATE), recipeProvider.has(RunicRitualsBlocks.RUNESLATE))
            .save(output);
    }

    void createInlayRecipe(RuneSymbol symbol, HolderLookup.Provider registries, RecipeProvider recipeProvider, RecipeOutput output) {

        List<Item> itemList = symbol.getMaterialsAllowed().stream().filter(i -> i != RuneInlayMaterial.ETCHED).map(RuneInlayMaterial::getAssociatedItem).toList();
        Item[] allowedItems = new Item[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            allowedItems[i] = itemList.get(i);
        }
        String symbolName = symbol.getName().toLowerCase(Locale.ROOT).replace(" ", "_");

        if(allowedItems.length == 0) {
            RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE.asItem())
                    .setOutputSymbol(symbol)
                    .setIdAffix("inlayed_" + symbolName)
                    .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                    .inlayMaterial(RuneEngravingRecipe.INLAYABLE_ITEMS)
                    .unlockedBy(getHasName(RunicRitualsBlocks.RUNESLATE), recipeProvider.has(RunicRitualsBlocks.RUNESLATE))
                    .save(output);
        } else {
            RuneEngravingRecipeBuilder.engraving(registries, RecipeCategory.MISC, RunicRitualsBlocks.RUNESLATE.asItem())
                .setOutputSymbol(symbol)
                .setIdAffix("inlayed_" + symbolName)
                .runeBase(RuneEngravingRecipe.ENGRAVABLE_ITEMS)
                .inlayMaterial(Ingredient.of(allowedItems))
                .unlockedBy(getHasName(RunicRitualsBlocks.RUNESLATE), recipeProvider.has(RunicRitualsBlocks.RUNESLATE))
                .save(output);
        }
    }

    @Override
    public String getName() {
        return "RunicRitualsRecipeProvider";
    }
}
