package net.runicrituals.registries;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngraver;

import java.util.function.Function;

public class RunicRitualsBlocks {

    public static final BlockItemId RUNE_ENGRAVER_KEY = createWithId("rune_engraver");
    public static final BlockItemId RUNESLATE_KEY = createWithId("runeslate");

    public static final Block RUNE_ENGRAVER = register(RUNE_ENGRAVER_KEY, RuneEngraver::new, BlockBehaviour.Properties.of().sound(SoundType.STONE).destroyTime(1.5f).noOcclusion());
    public static final Block RUNESLATE = register(RUNESLATE_KEY, Block::new, BlockBehaviour.Properties.of().sound(SoundType.STONE).destroyTime(1.5f).noOcclusion());

    public static void registerBlocks() {
        RunicRituals.LOGGER.info("Registering mod blocks");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((tab) -> tab.accept(RUNE_ENGRAVER) );
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((tab) -> tab.accept(RUNESLATE) );
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
}
