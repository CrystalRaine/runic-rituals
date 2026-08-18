package net.runicrituals.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.runicrituals.RunicRituals;

public class RunicRitualsStats {

    public static final Identifier INTERACT_WITH_RUNE_ENGRAVERS = register("interact_with_rune_engraver", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_RUNE_OBELISKS = register("interact_with_rune_obelisk", StatFormatter.DEFAULT);
    public static final Identifier RUNES_ENGRAVED = register("runes_engraved", StatFormatter.DEFAULT);

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier id = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, name, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }

    public static void registerStats() {
        RunicRituals.LOGGER.info("Registering stats");
    }
}
