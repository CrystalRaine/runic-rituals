package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.BiConsumer;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class RunicRitualsModelProvider extends FabricModelProvider {

    private static final TextureSlot RUNE_LAYER = TextureSlot.create("rune_layer");

    public RunicRitualsModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators generator) {

        createLightBlockModel(RunicRitualsBlocks.DECAYING_LIGHT, generator);
        createLightBlockModel(RunicRitualsBlocks.SHADOW, generator);
        createLightBlockModel(RunicRitualsBlocks.DECAYING_SHADOW, generator);

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {

        generateRuneslateModels(itemModelGenerators);

    }

    private void createLightBlockModel(Block forBlock, BlockModelGenerators generator) {
        ItemModel.Unbaked base = ItemModelUtils.plainModel(this.createFlatItemModel(forBlock.asItem(), generator));
        Map<Integer, ItemModel.Unbaked> overrides = new HashMap<>(16);
        PropertyDispatch.C1<MultiVariant, Integer> lightBlockPropertyDispatch = PropertyDispatch.initial(BlockStateProperties.LEVEL);

        for (int i = 0; i <= 15; i++) {
            String suffix = String.format(Locale.ROOT, "_%02d", i);
            Material texture = TextureMapping.getItemTexture(forBlock.asItem(), suffix);
            lightBlockPropertyDispatch.select(i, plainVariant(ModelTemplates.PARTICLE_ONLY.createWithSuffix(forBlock, suffix, TextureMapping.particle(texture), generator.modelOutput)));
            ItemModel.Unbaked overrideItem = ItemModelUtils.plainModel(
                    ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(forBlock.asItem(), suffix), TextureMapping.layer0(texture), generator.modelOutput)
            );
            overrides.put(i, overrideItem);
        }

        generator.itemModelOutput.accept(forBlock.asItem(), ItemModelUtils.selectBlockItemProperty(LightBlock.LEVEL, base, overrides));
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(forBlock).with(lightBlockPropertyDispatch));
    }

    public final Identifier createFlatItemModel(final Item item, BlockModelGenerators generator) {
        return ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), generator.modelOutput);
    }

    private void generateRuneslateModels(ItemModelGenerators itemModelGenerators) {
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