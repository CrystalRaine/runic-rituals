package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.runicrituals.logic.runes.element.Matter;
import net.runicrituals.registries.RunicRitualsItems;

import java.util.Arrays;
import java.util.List;

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
    SACRIFICE(9, ChatFormatting.DARK_AQUA, "Sacrifice Action",  RunicRitualsItems.SACRIFICE_RUNE),
    BIND(10, ChatFormatting.AQUA, "Bind Action",  RunicRitualsItems.BIND_RUNE),

//    Form Runes
    BOLT(11, ChatFormatting.GOLD, "Bolt Form",  RunicRitualsItems.BOLT_RUNE),
    SPHERE(12, ChatFormatting.GOLD, "Sphere Form",  RunicRitualsItems.SPHERE_RUNE),
    CUBE(13, ChatFormatting.GOLD, "Cube Form",  RunicRitualsItems.CUBE_RUNE),

//    Logical Runes
    REPEAT(14, ChatFormatting.WHITE, "Repeat Logic",  RunicRitualsItems.REPEAT_RUNE),
    CONTROL(15, ChatFormatting.WHITE, "Control Logic",  RunicRitualsItems.CONTROL_RUNE),
    ;

    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item symbolItem;

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
}
