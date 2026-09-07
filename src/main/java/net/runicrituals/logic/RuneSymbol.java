package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsItems;

import java.util.*;

public enum RuneSymbol  {
//    Elemental Runes
//    using ids rather than ordinals so that adding new ones can't break things,
//    as long as you don't update the old ones
    ARCANE(0,ChatFormatting.AQUA, "Arcane", RunicRitualsItems.ARCANE_RUNE),
    KINETIC(1, ChatFormatting.GRAY, "Kinetic", RunicRitualsItems.KINETIC_RUNE),
    THERMAL(2, ChatFormatting.RED, "Thermal", RunicRitualsItems.THERMAL_RUNE),
    ELECTRIC(3, ChatFormatting.YELLOW, "Electric", RunicRitualsItems.ELECTRIC_RUNE),
    LIGHT(4, ChatFormatting.WHITE, "Light", RunicRitualsItems.LIGHT_RUNE),
    MATTER(5, ChatFormatting.GOLD, "Matter", RunicRitualsItems.MATTER_RUNE),
    SPACE(6, ChatFormatting.LIGHT_PURPLE, "Space", RunicRitualsItems.SPACE_RUNE),
    TIME(7, ChatFormatting.GREEN, "Time",  RunicRitualsItems.TIME_RUNE),

//    Action Runes
    MANIFEST(8, ChatFormatting.BLUE, "Manifest Action", RunicRitualsItems.MANIFEST_RUNE),
    SACRIFICE(9, ChatFormatting.BLUE, "Sacrifice Action",  RunicRitualsItems.SACRIFICE_RUNE),

//    Form Runes
    CUBE(13, ChatFormatting.GOLD, "Prism Form",  RunicRitualsItems.PRISM_RUNE),
    SHEET(20, ChatFormatting.GOLD, "Sheet Form", RunicRitualsItems.SHEET_RUNE),

//    Logical Runes
    CONTROL(15, ChatFormatting.WHITE, "Control Logic",  RunicRitualsItems.CONTROL_RUNE),
    BIND(10, ChatFormatting.WHITE, "Bind Logic",  RunicRitualsItems.BIND_RUNE),
    BOLT(11, ChatFormatting.WHITE, "Bolt Logic",  RunicRitualsItems.BOLT_RUNE),
    GROW(16, ChatFormatting.WHITE, "Grow Logic",  RunicRitualsItems.GROW_RUNE),
    SHRINK(17, ChatFormatting.WHITE, "Shrink Logic",  RunicRitualsItems.SHRINK_RUNE),
;
    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item symbolItem;

    private static final Map<String, Identifier> identifiers = new HashMap<>();

    RuneSymbol(int id, ChatFormatting formatting, String name, Item symbolItem){
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        this.symbolItem = symbolItem;
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

    public Item getSymbolItem() {
        return symbolItem;
    }

    public static RuneSymbol getSymbolFromId(int id){
//        be a little fancier to prevent crashes : )
        List<RuneSymbol> candidates = Arrays.stream(RuneSymbol.values()).filter(e -> e.id == id).toList();
        if (!candidates.isEmpty()) {
            return candidates.getFirst();
        }
        return RuneSymbol.ARCANE;
    }

    public static RuneSymbol getSymbolFromItem(ItemStackTemplate resultItem){
//        be a little fancier to prevent crashes : )
        List<RuneSymbol> candidates = Arrays.stream(RuneSymbol.values()).filter(e -> resultItem.is(e.symbolItem)).toList();
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
        id = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "textures/item/" + getIdentifierName() + "_rune.png");
        identifiers.put(getIdentifierName(), id);
        return id;
    }
}
