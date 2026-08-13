package net.runicrituals;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import net.runicrituals.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunicRituals implements ModInitializer {
	public static final String MOD_ID = "runic-rituals";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModBlockEntities.registerBlockEntityTypes();
		ModCreativeTabs.registerCreativeTabs();
		ModMenuTypes.registerMenuTypes();
		ModScreens.registerScreens();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
