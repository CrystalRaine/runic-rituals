package net.runicrituals.registries.client_only;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.components.RuneSymbolItemModelProperty;

public class RunicRitualsComponentsClient {

    public static void registerComponents() {
        RunicRituals.LOGGER.info("Registering Client Components");

        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);

        RangeSelectItemModelProperties.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_symbol_data_model_properties"),
                RuneSymbolItemModelProperty.MAP_CODEC
        );
    }
}