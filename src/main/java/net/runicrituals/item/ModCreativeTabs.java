package net.runicrituals.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.RunicRituals;

public class ModCreativeTabs {

    public static final ResourceKey<CreativeModeTab> RUNIC_RITUALS_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "runic-rituals")
    );

    public static final CreativeModeTab RUNIC_RITUALS_TAB = FabricCreativeModeTab.builder()
        .icon(() -> new ItemStack(ModItems.BASIC_WAND))
        .title(Component.translatable("creativeTab.runic-rituals.mod"))
        .displayItems((params, output) -> {
            output.accept(ModItems.BASIC_WAND);
            output.accept(ModItems.RUNESTONE);
            output.accept(ModBlocks.RUNESLATE);
            output.accept(ModBlocks.RUNE_ENGRAVER);
        })
        .build();

    public static void registerCreativeTabs() {
        RunicRituals.LOGGER.info("Registering creative tabs");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, RUNIC_RITUALS_TAB_KEY, RUNIC_RITUALS_TAB);

    }
}
