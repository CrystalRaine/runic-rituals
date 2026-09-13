package net.runicrituals.registries;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.HoverTextComponent;
import net.runicrituals.registries.items.RunicRitualsItem;
import net.runicrituals.registries.items.WandItem;
import net.runicrituals.registries.server_only.RunicRitualsComponents;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class RunicRitualsItems {

    public static final ResourceKey<Item> BASIC_WAND_KEY = resourceKey("wand");
    public static final ResourceKey<Item> DIAMOND_WAND_KEY = resourceKey("diamond_wand");
    public static final ResourceKey<Item> ECHO_WAND_KEY = resourceKey("echo_wand");

    public static final Item BASIC_WAND = registerItem(BASIC_WAND_KEY,
            WandItem::new,
            new WandItem
                    .RunicRitualsItemProperties()
                    .wand(ToolMaterial.IRON, 1f, -2.5F)
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .enchantable(10)
                    .repairable(Items.COPPER_INGOT)
                    .component(RunicRitualsComponents.HOVER_TEXT_COMPONENT, new HoverTextComponent("wand"))
    );

    public static final Item DIAMOND_WAND = registerItem(DIAMOND_WAND_KEY,
            WandItem::new,
            new WandItem
                    .RunicRitualsItemProperties()
                    .wand(ToolMaterial.DIAMOND, 1f, -2.8F)
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .enchantable(20)
                    .repairable(Items.COPPER_INGOT)
                    .component(RunicRitualsComponents.HOVER_TEXT_COMPONENT, new HoverTextComponent("wand"))
    );

    public static final Item ECHO_WAND = registerItem(ECHO_WAND_KEY,
            WandItem::new,
            new WandItem
                    .RunicRitualsItemProperties()
                    .wand(ToolMaterial.NETHERITE, 1f, -3.0F)
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
                    .enchantable(30)
                    .repairable(Items.COPPER_INGOT)
                    .component(RunicRitualsComponents.HOVER_TEXT_COMPONENT, new HoverTextComponent("wand"))
                    .fireResistant()
    );

    public static void registerItems() {
        RunicRituals.LOGGER.info("Registering mod items");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((tab)-> {tab.accept(BASIC_WAND);});
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((tab)-> {tab.accept(DIAMOND_WAND);});
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((tab)-> {tab.accept(ECHO_WAND);});
    }

    public static Item registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = resourceKey(name);
        return registerItem(itemKey, itemFactory, settings);
    }

    public static Item registerItem(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name.toLowerCase(Locale.ROOT).replace(" ", "_")));
    }
}
