package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class RunicRitualsModelProvider extends FabricModelProvider {

    public static final ModelTemplate HALF_SCALE = item("half_scale", TextureSlot.LAYER0);

    public RunicRitualsModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {
    }

    private static ModelTemplate item(String parent, TextureSlot requiredTextureKeys) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    public static ItemModel.Unbaked registerScaledHalf(Item item, ItemModelGenerators generator) {
        Identifier itemModel = HALF_SCALE.create(item, TextureMapping.singleSlot(TextureSlot.LAYER0, new Material(ModelLocationUtils.getModelLocation(item))), generator.modelOutput);
        return ItemModelUtils.plainModel(itemModel);
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {

        ItemModel.Unbaked runestone = ItemModelUtils.plainModel(itemModelGenerator.createFlatItemModel(RunicRitualsItems.RUNESTONE, ModelTemplates.FLAT_ITEM));

        ItemModel.Unbaked kineticRune   =   registerScaledHalf(RunicRitualsItems.KINETIC_RUNE, itemModelGenerator);
        ItemModel.Unbaked arcaneRune    =   registerScaledHalf(RunicRitualsItems.ARCANE_RUNE, itemModelGenerator);
        ItemModel.Unbaked thermalRune   =   registerScaledHalf(RunicRitualsItems.THERMAL_RUNE, itemModelGenerator);
        ItemModel.Unbaked electricRune  =   registerScaledHalf(RunicRitualsItems.ELECTRIC_RUNE, itemModelGenerator);
        ItemModel.Unbaked lightRune     =   registerScaledHalf(RunicRitualsItems.LIGHT_RUNE, itemModelGenerator);
        ItemModel.Unbaked spaceRune     =   registerScaledHalf(RunicRitualsItems.SPACE_RUNE, itemModelGenerator);
        ItemModel.Unbaked timeRune      =   registerScaledHalf(RunicRitualsItems.TIME_RUNE, itemModelGenerator);
        ItemModel.Unbaked manifestRune  =   registerScaledHalf(RunicRitualsItems.MANIFEST_RUNE, itemModelGenerator);

        ItemModel.Unbaked runeSelect = ItemModelUtils.rangeSelect(
            new RuneSymbolItemModelProperty(),
            List.of(
                ItemModelUtils.override(arcaneRune,     0),
                ItemModelUtils.override(kineticRune,    1),
                ItemModelUtils.override(thermalRune,    2),
                ItemModelUtils.override(electricRune,   3),
                ItemModelUtils.override(lightRune,      4),
                ItemModelUtils.override(spaceRune,      5),
                ItemModelUtils.override(timeRune,       6),
                ItemModelUtils.override(manifestRune,   7)
            )
        );

        itemModelGenerator.generateBooleanDispatch(
            RunicRitualsItems.RUNESTONE,
            ItemModelUtils.hasComponent(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE),
            ItemModelUtils.composite(
                runestone,
                runeSelect
            ),
            runestone
        );
    }

    @Override
    public @NonNull String getName() {
        return "RunicRitualsModelProvider";
    }
}