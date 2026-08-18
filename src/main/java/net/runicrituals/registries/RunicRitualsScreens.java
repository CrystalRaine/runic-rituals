package net.runicrituals.registries;

import net.minecraft.client.gui.screens.MenuScreens;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngraverScreen;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskScreen;

public class RunicRitualsScreens {

    public static void registerScreens() {
        RunicRituals.LOGGER.info("Registering screens");

        MenuScreens.register(RunicRitualsMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, RuneEngraverScreen::new);
        MenuScreens.register(RunicRitualsMenuTypes.RUNE_OBELISK_MENU_TYPE, RuneObeliskScreen::new);
    }
}
