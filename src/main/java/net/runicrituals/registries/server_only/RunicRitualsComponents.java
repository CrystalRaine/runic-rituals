package net.runicrituals.registries.server_only;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.ActiveHoverTextComponent;
import net.runicrituals.registries.components.HoverTextComponent;
import net.runicrituals.registries.components.RuneDataComponent;

public class RunicRitualsComponents {

    public static final DataComponentType<RuneDataComponent> RUNE_DATA_COMPONENT_TYPE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "rune_data"),
        DataComponentType.<RuneDataComponent>builder()
                .persistent(RuneDataComponent.CODEC)
                .networkSynchronized(ByteBufCodecs.fromCodec(RuneDataComponent.CODEC))
                .build()
    );

    public static final DataComponentType<HoverTextComponent> HOVER_TEXT_COMPONENT = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "hover_text"),
        DataComponentType.<HoverTextComponent>builder()
            .persistent(HoverTextComponent.CODEC)
            .networkSynchronized(ByteBufCodecs.fromCodec(HoverTextComponent.CODEC))
            .build()
    );

    public static final DataComponentType<ActiveHoverTextComponent> ACTIVE_HOVER_TEXT_COMPONENT = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "active_hover_text"),
        DataComponentType.<ActiveHoverTextComponent>builder()
            .persistent(ActiveHoverTextComponent.CODEC)
            .networkSynchronized(ByteBufCodecs.fromCodec(ActiveHoverTextComponent.CODEC))
            .build()
    );
    
    public static final DataComponentType<BlockPos> BOUND_POSITION = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "position_binding"),
        DataComponentType.<BlockPos>builder()
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC)
            .build()
    );
    
    public static final DataComponentType<String> TARGET_ENTITY_REFERENCE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "target_entity_binding"),
        DataComponentType.<String>builder()
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.fromCodec(Codec.STRING))
            .build()
    );

    public static final DataComponentType<BlockPos> BOUND_CONTROL_POSITION = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "controlling_position_binding"),
        DataComponentType.<BlockPos>builder()
            .persistent(BlockPos.CODEC)
            .networkSynchronized(ByteBufCodecs.fromCodec(BlockPos.CODEC))
            .build()
    );

    public static final DataComponentType<Boolean> ACTIVE_ITEM = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "active_item"),
        DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.fromCodec(Codec.BOOL))
            .build()
    );

    public static void registerComponents() {
        RunicRituals.LOGGER.info("Registering Components");

        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.ACTIVE_HOVER_TEXT_COMPONENT);
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.DAMAGE, RunicRitualsComponents.HOVER_TEXT_COMPONENT);
    }
}