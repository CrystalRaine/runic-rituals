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
    ARCANE(0, RuneType.ELEMENT, ChatFormatting.AQUA, "Arcane", List.of(RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND)),
    KINETIC(1, RuneType.ELEMENT, ChatFormatting.GRAY, "Kinetic", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.ETCHED, RuneInlayMaterial.NETHERITE, RuneInlayMaterial.SOUL, RuneInlayMaterial.BREEZE)),
    THERMAL(2, RuneType.ELEMENT, ChatFormatting.RED, "Thermal", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.ICE)),
    ELECTRIC(3, RuneType.ELEMENT, ChatFormatting.YELLOW, "Electric", List.of(RuneInlayMaterial.GOLD, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.COPPER, RuneInlayMaterial.REDSTONE)),
    LIGHT(4, RuneType.ELEMENT, ChatFormatting.WHITE, "Light", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.OBSIDIAN)),
    MATTER(5, RuneType.ELEMENT, ChatFormatting.GOLD, "Matter", List.of(RuneInlayMaterial.NETHERITE, RuneInlayMaterial.OBSIDIAN)),
    SPACE(6, RuneType.ELEMENT, ChatFormatting.LIGHT_PURPLE, "Space", List.of(RuneInlayMaterial.ECHO, RuneInlayMaterial.CHORUS)),
    TIME(7, RuneType.ELEMENT, ChatFormatting.GREEN, "Time", List.of(RuneInlayMaterial.ECHO)),

//    Actions
    MANIFEST(8, RuneType.ACTION, ChatFormatting.BLUE, "Manifest Action"),
    SACRIFICE(9, RuneType.ACTION, ChatFormatting.BLUE, "Sacrifice Action"),
    ANIMATE(10, RuneType.ACTION, ChatFormatting.BLUE, "Animate Action"),

//    Forms
    CUBE(11, RuneType.FORM, ChatFormatting.GOLD, "Prism Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.GOLD, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.GLASS)),
    SHEET(12, RuneType.FORM, ChatFormatting.GOLD, "Sheet Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.GLASS)),
    DOME(13, RuneType.FORM, ChatFormatting.GOLD, "Dome Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.ICE, RuneInlayMaterial.BREEZE, RuneInlayMaterial.GLASS)),

//    Modifiers
    CONTROL(14, RuneType.MODIFIER, ChatFormatting.WHITE, "Control Activation", List.of(RuneInlayMaterial.NETHERITE, RuneInlayMaterial.ECHO, RuneInlayMaterial.CHORUS)),
    POWERED(15, RuneType.MODIFIER, ChatFormatting.WHITE, "Redstone Activation", List.of(RuneInlayMaterial.REDSTONE)),
    DELAY(16, RuneType.MODIFIER, ChatFormatting.WHITE, "Delay Activation", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.SOUL, RuneInlayMaterial.BREEZE)),
    BOUND(17, RuneType.MODIFIER, ChatFormatting.WHITE, "Bind Position", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.IRON, RuneInlayMaterial.ICE)),
    BOLT(18, RuneType.MODIFIER, ChatFormatting.WHITE, "Bolt Position", List.of(RuneInlayMaterial.BREEZE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    HIT(19, RuneType.MODIFIER, ChatFormatting.WHITE, "Hit Position", List.of(RuneInlayMaterial.BREEZE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.SOUL)),
    STATIC(20, RuneType.MODIFIER, ChatFormatting.WHITE, "Static Position", List.of(RuneInlayMaterial.ETCHED, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND)),
    GROW(21, RuneType.MODIFIER, ChatFormatting.WHITE, "Grow Modification", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    SHRINK(22, RuneType.MODIFIER, ChatFormatting.WHITE, "Shrink Modification", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.ICE, RuneInlayMaterial.SOUL)),

//    Mana Binding
    MANABOUND(23, RuneType.MANA_BOUND, ChatFormatting.WHITE, "Mana Bound", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.ENDER)),
;

    private final int id;
    private final RuneType runeType;
    private final String name;
    private final ChatFormatting formatting;
    private static final Map<String, Identifier> identifiers = new HashMap<>();
    private List<RuneInlayMaterial> materialsAllowed = new ArrayList<>();

    RuneSymbol(int id, RuneType type, ChatFormatting formatting, String name){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        this.runeType = type;
    }

    RuneSymbol(int id, RuneType type, ChatFormatting formatting, String name, List<RuneInlayMaterial> materialsAllowed){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
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
        return formatting;
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
