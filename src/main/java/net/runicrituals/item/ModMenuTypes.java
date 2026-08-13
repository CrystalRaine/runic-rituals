package net.runicrituals.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.runicrituals.RunicRituals;
import net.runicrituals.item.blocks.rune_engraver.RuneEngraverMenu;

public class ModMenuTypes {

    public static final MenuType<RuneEngraverMenu> RUNE_ENGRAVER_MENU_MENU_TYPE = register("rune_engraver", RuneEngraverMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> register(
            String name,
            MenuType.MenuSupplier<T> constructor
    ) {
        return Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name), new MenuType<>(constructor, FeatureFlagSet.of()));
    }

    public static void registerMenuTypes() {
        RunicRituals.LOGGER.info("Registering menu types");
    }
}