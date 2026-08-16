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
import net.runicrituals.registries.items.Runestone;

import java.util.Locale;
import java.util.function.Function;

public class RunicRitualsItems {

    public static final ResourceKey<Item> RUNESTONE_KEY = resourceKey("runestone");
    public static final ResourceKey<Item> KINETIC_RUNE_KEY = resourceKey("kinetic_rune");
    public static final ResourceKey<Item> ARCANE_RUNE_KEY = resourceKey("arcane_rune");
    public static final ResourceKey<Item> THERMAL_RUNE_KEY = resourceKey("thermal_rune");
    public static final ResourceKey<Item> ELECTRIC_RUNE_KEY = resourceKey("electric_rune");
    public static final ResourceKey<Item> LIGHT_RUNE_KEY = resourceKey("light_rune");
    public static final ResourceKey<Item> SPACE_RUNE_KEY = resourceKey("space_rune");
    public static final ResourceKey<Item> TIME_RUNE_KEY = resourceKey("time_rune");
    public static final ResourceKey<Item> MANIFEST_RUNE_KEY = resourceKey("manifest_rune");
    public static final ResourceKey<Item> SACRIFICE_RUNE_KEY = resourceKey("sacrifice_rune");

    public static final Item KINETIC_RUNE = registerItem(KINETIC_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item ARCANE_RUNE = registerItem(ARCANE_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item THERMAL_RUNE = registerItem(THERMAL_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item ELECTRIC_RUNE = registerItem(ELECTRIC_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item LIGHT_RUNE = registerItem(LIGHT_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item SPACE_RUNE = registerItem(SPACE_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item TIME_RUNE = registerItem(TIME_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item MANIFEST_RUNE = registerItem(MANIFEST_RUNE_KEY, Item::new, new Item.Properties());
    public static final Item SACRIFICE_RUNE = registerItem(SACRIFICE_RUNE_KEY, Item::new, new Item.Properties());


    public static final ResourceKey<Item> BASIC_WAND_KEY = resourceKey("wand");

    public static final Item RUNESTONE = registerItem(RUNESTONE_KEY, Runestone::new, new Item.Properties());
    public static final Item BASIC_WAND = registerItem(BASIC_WAND_KEY, Item::new, new Item.Properties().stacksTo(1));

    public static void registerItems() {
        RunicRituals.LOGGER.info("Registering mod items");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((tab)-> {tab.accept(RUNESTONE);});
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register((tab)-> {tab.accept(BASIC_WAND);});
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = resourceKey(name);
        return registerItem(itemKey, itemFactory, settings);
    }

    private static Item registerItem(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name.toLowerCase(Locale.ROOT).replace(" ", "_")));
    }
}
