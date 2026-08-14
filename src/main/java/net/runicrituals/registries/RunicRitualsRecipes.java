package net.runicrituals.registries;

import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngravingRecipe;

public class RunicRitualsRecipes {

    public static final RecipeSerializer<RuneEngravingRecipe> RUNE_ENGRAVER_RECIPE_RECIPE_SERIALIZER = Registry.register( BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_engraving"), new RecipeSerializer<>(RuneEngravingRecipe.CODEC, RuneEngravingRecipe.STREAM_CODEC));
    public static final RecipeType<RuneEngravingRecipe> RUNE_ENGRAVER_RECIPE_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_engraving"), new RecipeType<RuneEngravingRecipe>() { });

    public static void registerRecipes() {
        RunicRituals.LOGGER.info("Registering recipes");

        RecipeSynchronization.synchronizeRecipeSerializer(RUNE_ENGRAVER_RECIPE_RECIPE_SERIALIZER);
    }
}
