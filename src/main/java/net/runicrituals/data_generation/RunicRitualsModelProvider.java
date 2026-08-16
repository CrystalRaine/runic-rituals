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
import net.minecraft.world.item.Item;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
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

//        create list of runesymbols entries from the enum
        List<RangeSelectItemModel.Entry> unbakedRuneSymbols = new ArrayList<>();
        for(RuneSymbol symbol : RuneSymbol.values()) {
            Identifier halfScaleId = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "item/rune_overlay/" + symbol.name().toLowerCase());
            Identifier halfScaleModel = HALF_SCALE.create(
                    halfScaleId,
                    TextureMapping.singleSlot(TextureSlot.LAYER0, new Material(ModelLocationUtils.getModelLocation(symbol.getSymbolItem()))),
                    itemModelGenerator.modelOutput
            );
            unbakedRuneSymbols.add(ItemModelUtils.override(ItemModelUtils.plainModel(halfScaleModel), symbol.getId()));
        }

//        add the entries to a rangeselect
        ItemModel.Unbaked runeSelect = ItemModelUtils.rangeSelect(
            new RuneSymbolItemModelProperty(),
            unbakedRuneSymbols
        );

//        choose the rangeselect (composite on blank stone) or blank runestone depending on data
        itemModelGenerator.generateBooleanDispatch(
            RunicRitualsItems.RUNESTONE,
            ItemModelUtils.hasComponent(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE),
            ItemModelUtils.composite(
                runestone,
                runeSelect
            ),
            runestone
        );

        for(RuneSymbol symbol : RuneSymbol.values()) {
            itemModelGenerator.generateFlatItem(symbol.getSymbolItem(), ModelTemplates.FLAT_ITEM);
        }
    }

    @Override
    public @NonNull String getName() {
        return "RunicRitualsModelProvider";
    }
}