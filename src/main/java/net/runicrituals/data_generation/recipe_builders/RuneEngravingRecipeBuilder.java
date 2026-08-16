package net.runicrituals.data_generation.recipe_builders;

import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class RuneEngravingRecipeBuilder implements RecipeBuilder {

    private final ItemStackTemplate result;
    private Ingredient runeBase;
    private Ingredient inlayMaterial = null;
    private final RecipeCategory category;
    private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder(); // for unlockedBy()
    private final HolderGetter<Item> items;
    private String idAffix;


    private RuneEngravingRecipeBuilder(final HolderLookup.Provider registries, RecipeCategory category, ItemStackTemplate result) {
        this.items = registries.lookupOrThrow(Registries.ITEM);
        this.result = result;
        this.category = category;
    }

    public static RuneEngravingRecipeBuilder engraving(final HolderLookup.Provider registries, RecipeCategory category, ItemLike recipeOutput) {
        return new RuneEngravingRecipeBuilder(registries, category,
                new ItemStackTemplate(recipeOutput.asItem(), 1)
        );
    }

    public RuneEngravingRecipeBuilder runeBase(final TagKey<Item> runeBase) {
        this.runeBase = Ingredient.of(this.items.getOrThrow(runeBase));
        return this;
    }

    public RuneEngravingRecipeBuilder inlayMaterial(final TagKey<Item> inlayMaterial) {
        this.inlayMaterial = Ingredient.of(this.items.getOrThrow(inlayMaterial));
        return this;
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
        return this;
    }

    public RuneEngravingRecipeBuilder setIdAffix(String idAffix) {
        this.idAffix = idAffix;
        return this;
    }

    /**
     * Doesn't do anything; no groups in engraving table
     * @param group
     * @return this
     */
    @Override
    public @NonNull RecipeBuilder group(@Nullable String group) {
        return this;
    }

    /**
     * returns the default id / resource key for the given recipe
     * @return
     */
    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        if(idAffix != null) {
            String originalName = result.typeHolder().getRegisteredName();
            originalName = originalName.substring(originalName.lastIndexOf(':') + 1);
            return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, originalName + "_" + idAffix));
        } else {
            return ResourceKey.create(Registries.RECIPE, result.typeHolder().unwrapKey().orElseThrow().identifier());
        }
    }

    @Override
    public void save(final RecipeOutput output, final ResourceKey<Recipe<?>> id) {
        RuneEngravingRecipe recipe = new RuneEngravingRecipe(this.runeBase, Optional.ofNullable(this.inlayMaterial), this.result);
        output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
    }

}
