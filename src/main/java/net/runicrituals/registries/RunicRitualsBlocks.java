package net.runicrituals.registries;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.lights.DecayingLightBlock;
import net.runicrituals.registries.blocks.lights.DecayingShadowBlock;
import net.runicrituals.registries.blocks.lights.ShadowBlock;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngraver;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class RunicRitualsBlocks {

    public static final BlockItemId RUNE_ENGRAVER_KEY = createWithId("rune_engraver");
    public static final BlockItemId RUNESLATE_KEY = createWithId("runeslate");
    public static final BlockItemId DECAYING_LIGHT_KEY = createWithId("decaying_light");
    public static final BlockItemId SHADOW_KEY = createWithId("shadow");
    public static final BlockItemId DECAYING_SHADOW_KEY = createWithId("decaying_shadow");

    public static final Block RUNE_ENGRAVER = register(RUNE_ENGRAVER_KEY, RuneEngraver::new, BlockBehaviour.Properties.of().sound(SoundType.STONE).destroyTime(1.5f).noOcclusion());
    public static final Block RUNESLATE = register(RUNESLATE_KEY, Runeslate::new, BlockBehaviour.Properties.of().sound(SoundType.STONE).destroyTime(0.5f).noOcclusion());
    public static final Block DECAYING_LIGHT = register(
        DECAYING_LIGHT_KEY,
        DecayingLightBlock::new,
        BlockBehaviour.Properties.of()
            .replaceable()
            .strength(-1.0F, 3600000.8F)
            .mapColor(waterloggedMapColor(MapColor.NONE))
            .noLootTable()
            .noOcclusion()
            .lightLevel(LightBlock.LIGHT_EMISSION),
        p -> p.rarity(Rarity.EPIC).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(LightBlock.LEVEL, 15))

    );
    public static final Block SHADOW = register(
        SHADOW_KEY,
        ShadowBlock::new,
        BlockBehaviour.Properties.of()
            .replaceable()
            .strength(-1.0F, 3600000.8F)
            .mapColor(waterloggedMapColor(MapColor.NONE))
            .noLootTable(),
        p -> p.rarity(Rarity.EPIC).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(LightBlock.LEVEL, 15))
    );
    public static final Block DECAYING_SHADOW = register(
        DECAYING_SHADOW_KEY,
        DecayingShadowBlock::new,
        BlockBehaviour.Properties.of()
            .replaceable()
            .strength(-1.0F, 3600000.8F)
            .mapColor(waterloggedMapColor(MapColor.NONE))
            .noLootTable(),
        p -> p.rarity(Rarity.EPIC).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(LightBlock.LEVEL, 15))
    );

    public static void registerBlocks() {
        RunicRituals.LOGGER.info("Registering mod blocks");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((tab) -> tab.accept(RUNE_ENGRAVER));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((tab) -> tab.accept(RUNESLATE));
    }

    private static BlockItemId createWithId(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name);
        return BlockItemId.create(id, id);
    }

    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        Block block = blockFactory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        Block block = register(id.block(), blockFactory, properties);
        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

        return block;
    }

    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties, UnaryOperator<Item.Properties> propertiesFunction) {
        Block block = register(id.block(), blockFactory, properties);

        BiFunction<Block, Item.Properties, Item> itemFactory = (b, p) -> new BlockItem(b, propertiesFunction.apply(p));
        Item blockItem = itemFactory.apply(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

        return block;
    }

    private static Function<BlockState, MapColor> waterloggedMapColor(final MapColor mapColor) {
        return blockState -> blockState.getValue(BlockStateProperties.WATERLOGGED) ? MapColor.WATER : mapColor;
    }
}
