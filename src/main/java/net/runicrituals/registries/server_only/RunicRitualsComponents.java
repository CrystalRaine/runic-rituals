package net.runicrituals.registries.server_only;

import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.BlockPositionComponent;
import net.runicrituals.registries.components.ControlRuneStateComponent;
import net.runicrituals.registries.components.HoverTextComponent;
import net.runicrituals.registries.components.RuneDataComponent;

public class RunicRitualsComponents {

    public static final DataComponentType<RuneDataComponent> RUNE_DATA_COMPONENT_TYPE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_data"),
        DataComponentType.<RuneDataComponent>builder().persistent(RuneDataComponent.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(RuneDataComponent.CODEC)).build()
    );

    public static final DataComponentType<HoverTextComponent> HOVER_TEXT_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "hover_text"),
            DataComponentType.<HoverTextComponent>builder().persistent(HoverTextComponent.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(HoverTextComponent.CODEC)).build()
    );

    public static final DataComponentType<BlockPositionComponent> BOUND_POSITION = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "position_binding"),
            DataComponentType.<BlockPositionComponent>builder().persistent(BlockPositionComponent.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(BlockPositionComponent.CODEC)).build()
    );

    public static final DataComponentType<BlockPositionComponent> BOUND_CONTROL_POSITION = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "controlling_position_binding"),
            DataComponentType.<BlockPositionComponent>builder().persistent(BlockPositionComponent.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(BlockPositionComponent.CODEC)).build()
    );

    public static final DataComponentType<ControlRuneStateComponent> CONTROL_RUNE_STATE_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "control_value"),
            DataComponentType.<ControlRuneStateComponent>builder().persistent(ControlRuneStateComponent.CODEC).networkSynchronized(ByteBufCodecs.fromCodec(ControlRuneStateComponent.CODEC)).build()
    );

    public static void registerComponents() {
        RunicRituals.LOGGER.info("Registering Components");

        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.HOVER_TEXT_COMPONENT);
    }
}