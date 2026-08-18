package net.runicrituals.registries.blocks.rune_engraver;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsRecipes;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class RuneEngravingRecipe implements Recipe<RuneEngravingRecipeInput> {
    private final Ingredient runeBase;
    private final Optional<Ingredient> inlayMaterial;
    private final ItemStackTemplate result;

    public static final TagKey<Item> INLAYABLE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "inlay_items"));
    public static final TagKey<Item> ENGRAVABLE_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "engrave_items"));

    public static final MapCodec<RuneEngravingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("runeBase").forGetter(RuneEngravingRecipe::getRuneBase),
                    Ingredient.CODEC.optionalFieldOf("inlayMaterial").forGetter(RuneEngravingRecipe::getInlayMaterial),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(RuneEngravingRecipe::getResult)
            ).apply(instance, RuneEngravingRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RuneEngravingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RuneEngravingRecipe::getRuneBase,
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), RuneEngravingRecipe::getInlayMaterial,
            ItemStackTemplate.STREAM_CODEC, RuneEngravingRecipe::getResult,
            RuneEngravingRecipe::new
    );
    public RuneEngravingRecipe(Ingredient runeBase, Optional<Ingredient> inlayMaterial, ItemStackTemplate result) {
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

    public Optional<Ingredient> getInlayMaterial() {
        return this.inlayMaterial;
    }

    @Override
    public boolean matches(@NonNull RuneEngravingRecipeInput input, @NonNull Level level) {
        return this.runeBase.test(input.getItem(0)) && (this.inlayMaterial.isPresent() && this.inlayMaterial.get().test(input.getItem(1)) || (this.inlayMaterial.isEmpty() && input.getItem(1).isEmpty()));
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull RuneEngravingRecipeInput input) {

        // output from this workbench mostly ignores the recipe, as it only gives data for the rune to set into the engraved item.
        ItemStack recipeResult = input.getItem(0).copy();
        recipeResult.set(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE, new RuneDataComponent(input.getSymbol(), input.getItem(1).getItem()));
        recipeResult.setCount(1);

        return recipeResult;
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
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public SlotDisplay resultDisplay() {
        return new SlotDisplay.ItemStackSlotDisplay(result);
    }

}
