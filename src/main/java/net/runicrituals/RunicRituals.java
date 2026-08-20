package net.runicrituals;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import net.runicrituals.registries.*;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunicRituals implements ModInitializer {
	public static final String MOD_ID = "runic-rituals";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		RunicRitualsItems.registerItems();
		RunicRitualsBlocks.registerBlocks();
		RunicRitualsBlockEntities.registerBlockEntityTypes();
		RunicRitualsCreativeTabs.registerCreativeTabs();
		RunicRitualsMenuTypes.registerMenuTypes();
		RunicRitualsStats.registerStats();
		RunicRitualsRecipes.registerRecipes();
		RunicRitualsDamageTypes.registerDamageTypes();
		RunicRitualsComponents.registerComponents();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
