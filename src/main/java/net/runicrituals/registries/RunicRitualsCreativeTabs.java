package net.runicrituals.registries;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.runes.enums.RuneInlayMaterial;
import net.runicrituals.logic.runes.enums.RuneSymbol;
import net.runicrituals.registries.components.RuneDataComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;

import java.util.List;

public class RunicRitualsCreativeTabs {

    public static final ResourceKey<CreativeModeTab> RUNIC_RITUALS_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "runic_rituals")
    );

    public static final ResourceKey<CreativeModeTab> RUNES_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "runic_rituals_runes")
    );

    public static final CreativeModeTab RUNIC_RITUALS_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(RunicRitualsItems.BASIC_WAND))
            .title(Component.translatable("creativeTab.runic_rituals.mod"))
            .displayItems((params, output) -> {
                // Tools
                output.accept(RunicRitualsItems.BASIC_WAND);
                output.accept(RunicRitualsItems.DIAMOND_WAND);
                output.accept(RunicRitualsItems.ECHO_WAND);
                output.accept(RunicRitualsItems.MANAMETER);
                output.accept(RunicRitualsItems.BASIC_STAFF);
                output.accept(RunicRitualsItems.BOUND_RUNESTONE);

                // General
                output.accept(RunicRitualsBlocks.RUNESLATE);
                output.accept(RunicRitualsBlocks.RITUAL_ANCHOR);
                output.accept(RunicRitualsBlocks.RUNE_ENGRAVER);

                // Utility Blocks (created / used by some rune effects)
                output.accept(RunicRitualsBlocks.SHADOW);
                output.accept(RunicRitualsBlocks.DECAYING_SHADOW);
                output.accept(RunicRitualsBlocks.DECAYING_LIGHT);
            })
            .build();

    public static final CreativeModeTab RUNES_TAB = FabricCreativeModeTab.builder()
            .icon(() -> {
                ItemStack creativeTabIcon = new ItemStack(RunicRitualsBlocks.RUNESLATE.asItem());
                creativeTabIcon.set(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE, new RuneDataComponent(RuneSymbol.ARCANE.getId(), RuneInlayMaterial.ETCHED));
                return creativeTabIcon;
            })
            .title(Component.translatable("creativeTab.runic_rituals_runes.mod"))
            .displayItems((params, output) -> {
                output.accept(RunicRitualsBlocks.RUNESLATE.asItem());

                for(RuneSymbol symbol : RuneSymbol.values()) {
                    List<RuneInlayMaterial> materials = symbol.getMaterialsAllowed();
                    if(materials.isEmpty()) {
                        ItemStack typedRunestone = new ItemStack(RunicRitualsBlocks.RUNESLATE.asItem());
                        typedRunestone.set(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE, new RuneDataComponent(symbol, RuneInlayMaterial.ETCHED));

                        output.accept(typedRunestone);
                    }

                    for(RuneInlayMaterial material : materials) {
                        ItemStack typedRunestone = new ItemStack(RunicRitualsBlocks.RUNESLATE.asItem());
                        typedRunestone.set(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE, new RuneDataComponent(symbol,material));

                        output.accept(typedRunestone);
                    }
                }
            })
            .build();

    public static void registerCreativeTabs() {
        RunicRituals.LOGGER.info("Registering creative tabs");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, RUNIC_RITUALS_TAB_KEY, RUNIC_RITUALS_TAB);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, RUNES_TAB_KEY, RUNES_TAB);

    }
}
