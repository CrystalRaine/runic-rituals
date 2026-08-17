package net.runicrituals;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.runicrituals.data_generation.*;

public class RunicRitualsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(RunicRitualsEnglishLangProvider::new);
		pack.addProvider(RunicRitualsModelProvider::new);
		pack.addProvider(RunicRitualsTagProvider::new);
		pack.addProvider(RunicRitualsRecipeProvider::new);
		pack.addProvider(RunicRitualsLootTableProvider::new);
	}
}
