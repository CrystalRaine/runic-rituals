package net.runicrituals.item;

import net.minecraft.client.gui.screens.MenuScreens;
import net.runicrituals.RunicRituals;
import net.runicrituals.item.blocks.rune_engraver.RuneEngraverScreen;

public class ModScreens {

    public static void registerScreens() {
        RunicRituals.LOGGER.info("Registering screens");

        MenuScreens.register(ModMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, RuneEngraverScreen::new);
    }
}
