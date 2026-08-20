package net.runicrituals.registries.client_only;

import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;

public class RunicRitualsComponentsClient {

    public static void registerComponents() {
        RunicRituals.LOGGER.info("Registering Client Components");

        RangeSelectItemModelProperties.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_symbol_data_model_properties"),
                RuneSymbolItemModelProperty.MAP_CODEC
        );
    }
}