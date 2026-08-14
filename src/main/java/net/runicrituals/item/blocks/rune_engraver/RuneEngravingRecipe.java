package net.runicrituals.item.blocks.rune_engraver;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.runicrituals.item.RunicRitualsRecipes;
import org.jspecify.annotations.NonNull;

public class RuneEngravingRecipe implements Recipe<RuneEngravingRecipeInput> {
    private final Ingredient runeBase;
    private final Ingredient inlayMaterial;
    private final ItemStackTemplate result;

    public static final MapCodec<RuneEngravingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("runeBase").forGetter(RuneEngravingRecipe::getRuneBase),
                    Ingredient.CODEC.fieldOf("inlayMaterial").forGetter(RuneEngravingRecipe::getInlayMaterial),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(RuneEngravingRecipe::getResult)
            ).apply(instance, RuneEngravingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RuneEngravingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RuneEngravingRecipe::getRuneBase,
            Ingredient.CONTENTS_STREAM_CODEC, RuneEngravingRecipe::getInlayMaterial,
            ItemStackTemplate.STREAM_CODEC, RuneEngravingRecipe::getResult,
            RuneEngravingRecipe::new
    );

    public RuneEngravingRecipe(Ingredient runeBase, Ingredient inlayMaterial, ItemStackTemplate result) {
        this.runeBase = runeBase;
        this.inlayMaterial = inlayMaterial;
        this.result = result;
    }

    public ItemStackTemplate getResult() {
        return this.result;
    }

    public Ingredient getRuneBase() {
        return this.runeBase;
    }

    public Ingredient getInlayMaterial() {
        return this.inlayMaterial;
    }

    @Override
    public boolean matches(@NonNull RuneEngravingRecipeInput input, @NonNull Level level) {
        return this.runeBase.test(input.getItem(0)) && this.inlayMaterial.test(input.getItem(1));
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull RuneEngravingRecipeInput input) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "rune_engraving";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<RuneEngravingRecipeInput>> getSerializer() {
        return RunicRitualsRecipes.RUNE_ENGRAVER_RECIPE_RECIPE_SERIALIZER;
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<RuneEngravingRecipeInput>> getType() {
        return RunicRitualsRecipes.RUNE_ENGRAVER_RECIPE_RECIPE_TYPE;
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
