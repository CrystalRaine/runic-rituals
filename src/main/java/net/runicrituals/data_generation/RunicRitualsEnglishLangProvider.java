package net.runicrituals.data_generation;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.runicrituals.logic.RuneSymbol;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class RunicRitualsEnglishLangProvider extends FabricLanguageProvider {
    public RunicRitualsEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, @NonNull TranslationBuilder translationBuilder) {

        RuneSymbol[] runeSymbols = RuneSymbol.values();

        for(RuneSymbol s : runeSymbols) {
            translationBuilder.add("item.runic-rituals." + s.getName().toLowerCase(Locale.ROOT) + "_rune", s.getName() + " Rune");
        }

        translationBuilder.add("item.runic-rituals.runestone",                      "Runestone");
        translationBuilder.add("item.runic-rituals.wand",                           "Wand");
        translationBuilder.add("item.runic-rituals.element.rune_type_tooltip",      "Rune: %1$s");
        translationBuilder.add("item.runic-rituals.element.rune_inlay_tooltip",     "Inlay: %1$s");
        
        translationBuilder.add("block.runic-rituals.rune_engraver",                 "Rune Engraver");
        translationBuilder.add("block.runic-rituals.runeslate",                     "Runeslate");
        translationBuilder.add("block.runic-rituals.rune_obelisk",                  "Rune Obelisk");

        translationBuilder.add("creativeTab.runic_rituals.mod",                     "Runic Rituals");
        translationBuilder.add("creativeTab.runic_rituals_runes.mod",               "Runes");

        translationBuilder.add("stat.runic-rituals.runes_engraved",                 "Runes Engraved");
        translationBuilder.add("stat.runic-rituals.interact_with_rune_engraver",    "Interactions with Rune Engraver");

        translationBuilder.add("tag.item.runic-rituals.engrave_items",              "Engravable Items");
        translationBuilder.add("tag.item.runic-rituals.inlay_items",                "Rune Inlay Items");
    }
}