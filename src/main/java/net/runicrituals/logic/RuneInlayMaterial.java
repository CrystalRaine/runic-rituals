package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;

public enum RuneInlayMaterial {

    ETCHED(0, ChatFormatting.WHITE, "Etched", null),
    COPPER(1, ChatFormatting.RED, "Copper", Items.COPPER_INGOT),
    GOLD(2, ChatFormatting.GOLD, "Gold", Items.GOLD_INGOT),
    IRON(3, ChatFormatting.GRAY, "Iron", Items.IRON_INGOT),
    DIAMOND(4, ChatFormatting.AQUA, "Diamond", Items.DIAMOND),
    GLASS(5, ChatFormatting.GREEN, "Glass", Items.GLASS),
    OBSIDIAN(6, ChatFormatting.DARK_PURPLE, "Obsidian", Items.OBSIDIAN),
    NETHERITE(7, ChatFormatting.DARK_RED, "Netherite", Items.NETHERITE_SCRAP),
    ECHO(8, ChatFormatting.DARK_BLUE, "Echo", Items.ECHO_SHARD),
    AMETHYST(9, ChatFormatting.BLUE, "Amethyst", Items.AMETHYST_SHARD);

    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item associatedItem;

    RuneInlayMaterial(int id, ChatFormatting formatting, String name, Item item) {
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        associatedItem = item;
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
