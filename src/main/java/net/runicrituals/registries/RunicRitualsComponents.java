package net.runicrituals.registries;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.RuneDataComponent;

public class RunicRitualsComponents {

    public static final DataComponentType<RuneDataComponent> RUNE_DATA_COMPONENT_TYPE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_data"),
        DataComponentType.<RuneDataComponent>builder().persistent(RuneDataComponent.CODEC).build()
    );

    public static void registerComponents() {
        RunicRituals.LOGGER.info("Registering Components");

        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
    }
}