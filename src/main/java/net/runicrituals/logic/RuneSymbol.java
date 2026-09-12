package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;

import java.util.*;

public enum RuneSymbol implements StringRepresentable {
//    Elemental Runes
//    using ids rather than ordinals so that adding new ones can't break things,
//    as long as you don't update the old ones
    ARCANE(0,ChatFormatting.AQUA, "Arcane", List.of(RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND)),
    KINETIC(1, ChatFormatting.GRAY, "Kinetic", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.ETCHED, RuneInlayMaterial.NETHERITE, RuneInlayMaterial.SOUL, RuneInlayMaterial.BREEZE)),
    THERMAL(2, ChatFormatting.RED, "Thermal", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.ICE)),
    ELECTRIC(3, ChatFormatting.YELLOW, "Electric", List.of(RuneInlayMaterial.GOLD, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.COPPER, RuneInlayMaterial.REDSTONE)),
    LIGHT(4, ChatFormatting.WHITE, "Light", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.AMETHYST, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.OBSIDIAN)),
    MATTER(5, ChatFormatting.GOLD, "Matter", List.of(RuneInlayMaterial.NETHERITE, RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.ETCHED)),
    SPACE(6, ChatFormatting.LIGHT_PURPLE, "Space", List.of(RuneInlayMaterial.ECHO, RuneInlayMaterial.CHORUS)),
    TIME(7, ChatFormatting.GREEN, "Time", List.of(RuneInlayMaterial.ECHO)),

//    Action Runes
    MANIFEST(8, ChatFormatting.BLUE, "Manifest Action"),
    SACRIFICE(9, ChatFormatting.BLUE, "Sacrifice Action"),

//    Form Runes
    CUBE(13, ChatFormatting.GOLD, "Prism Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.GOLD, RuneInlayMaterial.DIAMOND, RuneInlayMaterial.GLASS)),
    SHEET(20, ChatFormatting.GOLD, "Sheet Form", List.of(RuneInlayMaterial.IRON, RuneInlayMaterial.COPPER, RuneInlayMaterial.BLAZE, RuneInlayMaterial.GLASS)),

//    Logical Runes
    CONTROL(15, ChatFormatting.WHITE, "Control Logic", List.of(RuneInlayMaterial.NETHERITE, RuneInlayMaterial.ECHO, RuneInlayMaterial.CHORUS)),
    BIND(10, ChatFormatting.WHITE, "Bind Logic", List.of(RuneInlayMaterial.OBSIDIAN, RuneInlayMaterial.IRON, RuneInlayMaterial.ICE)),
    BOLT(11, ChatFormatting.WHITE, "Bolt Logic", List.of(RuneInlayMaterial.BREEZE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    GROW(16, ChatFormatting.WHITE, "Grow Logic", List.of(RuneInlayMaterial.REDSTONE, RuneInlayMaterial.BLAZE, RuneInlayMaterial.CHORUS)),
    SHRINK(17, ChatFormatting.WHITE, "Shrink Logic", List.of(RuneInlayMaterial.GLASS, RuneInlayMaterial.ICE, RuneInlayMaterial.SOUL)),
;
    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private static final Map<String, Identifier> identifiers = new HashMap<>();
    private List<RuneInlayMaterial> materialsAllowed = new ArrayList<>();

    RuneSymbol(int id, ChatFormatting formatting, String name){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
    }

    RuneSymbol(int id, ChatFormatting formatting, String name, List<RuneInlayMaterial> materialsAllowed){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        this.materialsAllowed = materialsAllowed;
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
