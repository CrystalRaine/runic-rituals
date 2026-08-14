package net.runicrituals.registries;

import net.minecraft.client.gui.screens.MenuScreens;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_engraver.RuneEngraverScreen;

public class RunicRitualsScreens {

    public static void registerScreens() {
        RunicRituals.LOGGER.info("Registering screens");

        MenuScreens.register(RunicRitualsMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, RuneEngraverScreen::new);
    }
}
