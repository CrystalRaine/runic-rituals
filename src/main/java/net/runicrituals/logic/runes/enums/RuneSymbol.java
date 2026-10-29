package net.runicrituals.logic.runes.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.runicrituals.RunicRituals;
import org.jspecify.annotations.NonNull;

import java.util.*;

public enum RuneSymbol implements StringRepresentable {

//    using ids rather than ordinals so that adding new ones can't break things,
//    as long as you don't update the old ones (I re-arrange/add/remove these a lot)

//    Elemental
    ARCANE(0, RuneType.ELEMENT, "Arcane", List.of(RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND)),
    KINETIC(1, RuneType.ELEMENT, "Kinetic", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.ETCHED, RuneInlayMaterial.NETHERITE, RuneInlayMaterial.SOUL1, RuneInlayMaterial.SOUL2, RuneInlayMaterial.BREEZE)),
    THERMAL(2, RuneType.ELEMENT, "Thermal", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.ICE)),
    ELECTRIC(3, RuneType.ELEMENT, "Electric", List.of(RuneInlayMaterial.GOLD, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.COPPER, RuneInlayMaterial.REDSTONE)),
    LIGHT(4, RuneType.ELEMENT, "Light", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.OBSIDIAN)),
    MATTER(5, RuneType.ELEMENT, "Matter", List.of(RuneInlayMaterial.NETHERITE, RuneInlayMaterial.OBSIDIAN)),
    SPACE(6, RuneType.ELEMENT, "Space", List.of(RuneInlayMaterial.ECHO, RuneInlayMaterial.CHORUS)),
    TIME(7, RuneType.ELEMENT, "Time", List.of(RuneInlayMaterial.ECHO)),

//    Actions
    MANIFEST(8, RuneType.ACTION, "Manifest Action"),
    SACRIFICE(9, RuneType.ACTION, "Sacrifice Action"),
    ANIMATE(10, RuneType.ACTION, "Animate Action", List.of(RuneInlayMaterial.SOUL1, RuneInlayMaterial.SOUL2, RuneInlayMaterial.RESIN, RuneInlayMaterial.ECHO)),

//    Forms
    CUBE(11, RuneType.FORM, "Prism Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.GOLD, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.GLASS)),
    SHEET(12, RuneType.FORM, "Sheet Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.GLASS)),
    DOME(13, RuneType.FORM, "Dome Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.ICE, RuneInlayMaterial.BREEZE, RuneInlayMaterial.GLASS)),

//    Modifiers
    POWERED(15, RuneType.MODIFIER, "Redstone Activation", List.of(RuneInlayMaterial.REDSTONE)),
    DELAY(16, RuneType.MODIFIER, "Delay Activation", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.SOUL1, RuneInlayMaterial.SOUL2, RuneInlayMaterial.BREEZE)),
    BOUND(17, RuneType.MODIFIER, "Bind Position", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.IRON, RuneInlayMaterial.ICE)),
    BOLT(18, RuneType.MODIFIER, "Bolt Position", List.of(RuneInlayMaterial.BREEZE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    HIT(19, RuneType.MODIFIER, "Hit Position", List.of(RuneInlayMaterial.BREEZE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.SOUL1, RuneInlayMaterial.SOUL2)),
    GROW(21, RuneType.MODIFIER, "Grow Modification", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    SHRINK(22, RuneType.MODIFIER, "Shrink Modification", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.ICE, RuneInlayMaterial.SOUL1, RuneInlayMaterial.SOUL2)),

//    Mana Binding
    MANABOUND(23, RuneType.MANA_BOUND, "Mana Bound", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.ENDER, RuneInlayMaterial.CHORUS)),
;

    private final int id;
    private final RuneType runeType;
    private final String name;
    private static final Map<String, Identifier> identifiers = new HashMap<>();
    private List<RuneInlayMaterial> materialsAllowed = new ArrayList<>();

    RuneSymbol(int id, RuneType type, String name){
        this.id = id;
        this.name = name;
        this.runeType = type;
    }

    RuneSymbol(int id, RuneType type, String name, List<RuneInlayMaterial> materialsAllowed){
        this.id = id;
        this.name = name;
        this.materialsAllowed = materialsAllowed;
        this.runeType = type;
    }

    public RuneType getRuneType() {
        return runeType;
    }

    public List<RuneInlayMaterial> getMaterialsAllowed() {
        return materialsAllowed;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public ChatFormatting getFormatting() {
        switch (runeType) {
            case FORM -> {
                return ChatFormatting.GOLD;
            }
            case ELEMENT -> {
                return ChatFormatting.RED;
            }
            case ACTION -> {
                return ChatFormatting.BLUE;
            }
            case ANCHOR -> {
                return ChatFormatting.GREEN;
            }
            case MODIFIER -> {
                return ChatFormatting.WHITE;
            }
            case MANA_BOUND -> {
                return ChatFormatting.LIGHT_PURPLE;
            }
            case null, default -> {
                return ChatFormatting.STRIKETHROUGH;
            }
        }
    }

    public static RuneSymbol getSymbolFromId(int id){
//        be a little fancier to prevent crashes : )
        List<RuneSymbol> candidates = Arrays.stream(RuneSymbol.values()).filter(e -> e.id == id).toList();
        if (!candidates.isEmpty()) {
            return candidates.getFirst();
        }
        return RuneSymbol.ARCANE;
    }

    public static String getNameFromElementId(int id) {
        return getSymbolFromId(id).getName();
    }

    public static ChatFormatting getFormattingFromElementId(int id) {
        return getSymbolFromId(id).getFormatting();
    }

    public String getIdentifierName(){
        return name.toLowerCase(Locale.ROOT).replace(" ", "_");
    }

    /**
     * get the Identifier for the rune's image. this caches all rune images in a map to avoid re-creating identifiers with the same path/name
     * @return rune image Identifier
     */
    public Identifier getImageIdentifier() {

        Identifier id = identifiers.get(getIdentifierName());
        if(id != null) {
            return id;
        }
        id = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "textures/block/" + getIdentifierName() + "_rune.png");
        identifiers.put(getIdentifierName(), id);
        return id;
    }

    @Override
    public @NonNull String getSerializedName() {
        return getIdentifierName();
    }
}
