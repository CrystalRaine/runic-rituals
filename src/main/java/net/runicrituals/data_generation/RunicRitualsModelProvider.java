package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class RunicRitualsModelProvider extends FabricModelProvider {

    private static final TextureSlot RUNE_LAYER = TextureSlot.create("rune_layer");

    public RunicRitualsModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators generator) {

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {

        Identifier baseModelLoc = ModelLocationUtils.getModelLocation(RunicRitualsBlocks.RUNESLATE);
        ItemModel.Unbaked baseModel = ItemModelUtils.plainModel(baseModelLoc);

        List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();

        List<RuneSymbol> sortedSymbols = Arrays.stream(RuneSymbol.values())
            .sorted(Comparator.comparingInt(RuneSymbol::getId))
            .toList();

        ModelTemplate runeSlateTemplate = new ModelTemplate(
            Optional.of(baseModelLoc),
            Optional.empty(),
            RUNE_LAYER
        );

        for (RuneSymbol symbol : sortedSymbols) {

            Identifier textureLocation = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "block/" + symbol.getIdentifierName() + "_rune");
            Material textureMaterial = new Material(textureLocation);
            TextureMapping textureMapping = new TextureMapping().put(RUNE_LAYER, textureMaterial);


            Identifier childModelLoc = Identifier.fromNamespaceAndPath(
                    RunicRituals.MOD_ID,
                    "item/runeslate_" + symbol.getIdentifierName()
            );

            runeSlateTemplate.create(
                    childModelLoc,
                    textureMapping,
                    itemModelGenerators.modelOutput
            );

            overrides.add(ItemModelUtils.override(
                    ItemModelUtils.plainModel(childModelLoc),
                    (float) symbol.getId()
            ));
        }

        itemModelGenerators.itemModelOutput.accept(
            RunicRitualsBlocks.RUNESLATE.asItem(),
            ItemModelUtils.rangeSelect(
                new RuneSymbolItemModelProperty(),
                baseModel,
                overrides
            )
        );
    }

    @Override
    public @NonNull String getName() {
        return "RunicRitualsModelProvider";
    }
}