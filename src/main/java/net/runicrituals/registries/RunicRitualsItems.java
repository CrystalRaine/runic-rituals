package net.runicrituals.registries;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.runicrituals.RunicRituals;

import java.util.Locale;
import java.util.function.Function;

public class RunicRitualsItems {

    public static final ResourceKey<Item> BASIC_WAND_KEY = resourceKey("wand");

    public static final Item BASIC_WAND = registerItem(BASIC_WAND_KEY, Item::new, new Item.Properties().stacksTo(1));

    public static void registerItems() {
        RunicRituals.LOGGER.info("Registering mod items");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((tab)-> {tab.accept(BASIC_WAND);});
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
