package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.runicrituals.registries.RunicRitualsItems;
import org.jspecify.annotations.NonNull;

public class RunicRitualsModelProvider extends FabricModelProvider {
    public RunicRitualsModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {

        itemModelGenerator.generateFlatItem(RunicRitualsItems.RUNESTONE, ModelTemplates.FLAT_ITEM);
    }

    @Override
    public @NonNull String getName() {
        return "RunicRitualsModelProvider";
    }
}