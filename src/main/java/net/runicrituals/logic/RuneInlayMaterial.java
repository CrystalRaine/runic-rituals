package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;

public enum RuneInlayMaterial {

//    T0
    ETCHED(0, ChatFormatting.WHITE, "Etched", null, 0),

//    T1
    GLASS(11, ChatFormatting.GREEN, "Glass", Items.GLASS, 1),
    AMETHYST(12, ChatFormatting.GREEN, "Amethyst", Items.AMETHYST_SHARD, 1),
    ICE(13, ChatFormatting.GREEN, "Ice", Items.BLUE_ICE, 1),
    SOUL(14, ChatFormatting.GREEN, "Soul", Items.SOUL_SAND, 1),
    REDSTONE(26, ChatFormatting.BLUE, "Redstone", Items.REDSTONE, 2),

//    T2
    BLAZE(21, ChatFormatting.BLUE, "Blaze", Items.BLAZE_ROD, 2),
    BREEZE(22, ChatFormatting.BLUE, "Breeze", Items.BREEZE_ROD, 2),
    COPPER(23, ChatFormatting.BLUE, "Copper", Items.COPPER_INGOT, 2),
    IRON(25, ChatFormatting.BLUE, "Iron", Items.IRON_INGOT, 2),
    OBSIDIAN(33, ChatFormatting.DARK_PURPLE, "Obsidian", Items.OBSIDIAN, 3),

//    T3
    DIAMOND(31, ChatFormatting.DARK_PURPLE, "Diamond", Items.DIAMOND, 3),
    CHORUS(32, ChatFormatting.DARK_PURPLE, "Chorus", Items.CHORUS_FRUIT, 3),
    GOLD(24, ChatFormatting.BLUE, "Gold", Items.GOLD_INGOT, 2),

//    T4
    ECHO(41, ChatFormatting.GOLD, "Echo", Items.ECHO_SHARD, 4),
    NETHERITE(42, ChatFormatting.GOLD, "Netherite", Items.NETHERITE_SCRAP, 4)

    ;

    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item associatedItem;
    private final double efficiency;

    RuneInlayMaterial(int id, ChatFormatting formatting, String name, Item item, int tier) {
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        associatedItem = item;

        switch (tier) {
            case 0 -> this.efficiency = 0.1;
            case 1 -> this.efficiency = 0.3;
            case 2 -> this.efficiency = 0.5;
            case 3 -> this.efficiency = 0.70;
            case 4 -> this.efficiency = 0.95;

            default -> this.efficiency = 0.99;
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
