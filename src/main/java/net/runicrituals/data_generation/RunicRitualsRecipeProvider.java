package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class RunicRitualsRecipeProvider extends FabricRecipeProvider {

    public RunicRitualsRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
//                shapeless(RecipeCategory.MISC, RunicRitualsItems.RUNESTONE)
//                        .group("rune_engraving")
//                        .requires(RunicRitualsItems.RUNESTONE)
//                        .requires(RunicRitualsTagProvider.INLAYABLE_ITEMS)
//                        .unlockedBy(getHasName(RunicRitualsItems.RUNESTONE), has(RunicRitualsItems.RUNESTONE))
//                        .save(output);
//                ;
            }
        };
    }

    @Override
    public String getName() {
        return "RunicRitualsRecipeProvider";
    }
}
