package net.runicrituals.logic.runes.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;

public enum RuneInlayMaterial {

//    T0
    ETCHED(0, "Etched", null, 0),

//    T1
    GLASS(11, "Glass", Items.GLASS, 1),
    AMETHYST(12, "Amethyst", Items.AMETHYST_SHARD, 1),
    ICE(13, "Ice", Items.BLUE_ICE, 1),
    SOUL(14, "Soul", Items.SOUL_SAND, 1),
    REDSTONE(15, "Redstone", Items.REDSTONE, 1),

//    T2
    BLAZE(21, "Blaze", Items.BLAZE_ROD, 2),
    BREEZE(22, "Breeze", Items.BREEZE_ROD, 2),
    COPPER(23, "Copper", Items.COPPER_INGOT, 2),
    IRON(24, "Iron", Items.IRON_INGOT, 2),
    OBSIDIAN(25, "Obsidian", Items.OBSIDIAN, 2),
    ENDER(26, "Ender", Items.ENDER_PEARL, 2),

//    T3
    DIAMOND(31, "Diamond", Items.DIAMOND, 3),
    CHORUS(32, "Chorus", Items.CHORUS_FRUIT, 3),
    GOLD(33, "Gold", Items.GOLD_INGOT, 3),

//    T4
    ECHO(41, "Echo", Items.ECHO_SHARD, 4),
    NETHERITE(42, "Netherite", Items.NETHERITE_SCRAP, 4)

    ;

    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item associatedItem;
    private final double efficiency;

    RuneInlayMaterial(int id, String name, Item item, int tier) {
        this.id = id;
        this.name = name;
        associatedItem = item;

        switch (tier) {
            case 0 -> {
                this.efficiency = 0.1;
                this.formatting = ChatFormatting.WHITE;
            }
            case 1 -> {
                this.efficiency = 0.3;
                this.formatting = ChatFormatting.GREEN;
            }
            case 2 -> {
                this.efficiency = 0.5;
                this.formatting = ChatFormatting.BLUE;
            }
            case 3 -> {
                this.efficiency = 0.75;
                this.formatting = ChatFormatting.DARK_PURPLE;
            }
            case 4 -> {
                this.efficiency = 0.95;
                this.formatting = ChatFormatting.GOLD;
            }
            default -> {
                this.efficiency = 0.99;
                this.formatting = ChatFormatting.DARK_RED;
            }
        }
    }

    public double getEfficiency() {
        return efficiency;
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
    public Item getAssociatedItem() {
        return associatedItem;
    }

    public static RuneInlayMaterial getElementFromId(int id){
//        be a little fancier to prevent crashes : )
        List<RuneInlayMaterial> candidates = Arrays.stream(RuneInlayMaterial.values()).filter(e -> e.id == id).toList();
        if (!candidates.isEmpty()) {
            return candidates.getFirst();
        }
        return ETCHED;
    }

    public static String getNameFromElementId(int id) {
        return getElementFromId(id).getName();
    }

    public static ChatFormatting getFormattingFromElementId(int id) {
        return getElementFromId(id).getFormatting();
    }

    public static RuneInlayMaterial getByMaterial(Item item) {

        List<RuneInlayMaterial> candidates = Arrays.stream(RuneInlayMaterial.values()).filter(e -> e.associatedItem == item).toList();
        if (!candidates.isEmpty()) {
            return candidates.getFirst();
        }
        return ETCHED;
    }
}
