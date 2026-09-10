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
    ARCANE(0,ChatFormatting.AQUA, "Arcane"),
    KINETIC(1, ChatFormatting.GRAY, "Kinetic"),
    THERMAL(2, ChatFormatting.RED, "Thermal"),
    ELECTRIC(3, ChatFormatting.YELLOW, "Electric"),
    LIGHT(4, ChatFormatting.WHITE, "Light"),
    MATTER(5, ChatFormatting.GOLD, "Matter"),
    SPACE(6, ChatFormatting.LIGHT_PURPLE, "Space"),
    TIME(7, ChatFormatting.GREEN, "Time"),

//    Action Runes
    MANIFEST(8, ChatFormatting.BLUE, "Manifest Action"),
    SACRIFICE(9, ChatFormatting.BLUE, "Sacrifice Action"),

//    Form Runes
    CUBE(13, ChatFormatting.GOLD, "Prism Form"),
    SHEET(20, ChatFormatting.GOLD, "Sheet Form"),

//    Logical Runes
    CONTROL(15, ChatFormatting.WHITE, "Control Logic"),
    BIND(10, ChatFormatting.WHITE, "Bind Logic"),
    BOLT(11, ChatFormatting.WHITE, "Bolt Logic"),
    GROW(16, ChatFormatting.WHITE, "Grow Logic"),
    SHRINK(17, ChatFormatting.WHITE, "Shrink Logic"),
;
    private final int id;
    private final String name;
    private final ChatFormatting formatting;

    private static final Map<String, Identifier> identifiers = new HashMap<>();

    RuneSymbol(int id, ChatFormatting formatting, String name){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
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
