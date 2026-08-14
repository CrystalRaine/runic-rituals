package net.runicrituals.logic;

import net.minecraft.ChatFormatting;

import java.util.Arrays;
import java.util.List;

public enum RuneSymbol  {
    ARCANE(0,ChatFormatting.AQUA, "Arcane"),
    KINETIC(1, ChatFormatting.GRAY, "Kinetic"),
    THERMAL(2, ChatFormatting.RED, "Thermal"),
    ELECTRIC(3, ChatFormatting.YELLOW, "Electric"),
    LIGHT(4, ChatFormatting.WHITE, "Light"),
    SPACE(5, ChatFormatting.LIGHT_PURPLE, "Space"),
    TIME(6, ChatFormatting.GREEN, "Time"),
    MANIFEST(7, ChatFormatting.BLUE, "Manifest");

    private final int id;
    private final String name;
    private final ChatFormatting formatting;

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

    public static RuneSymbol getElementFromId(int id){
//        be a little fancier to prevent crashes : )
        List<RuneSymbol> candidates = Arrays.stream(RuneSymbol.values()).filter(e -> e.id == id).toList();
        if (!candidates.isEmpty()) {
            return candidates.getFirst();
        }
        return RuneSymbol.ARCANE;
    }

    public static String getNameFromElementId(int id) {
        return getElementFromId(id).getName();
    }

    public static ChatFormatting getFormattingFromElementId(int id) {
        return getElementFromId(id).getFormatting();
    }
}
