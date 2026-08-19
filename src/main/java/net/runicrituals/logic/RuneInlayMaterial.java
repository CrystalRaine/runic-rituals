package net.runicrituals.logic;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;

public enum RuneInlayMaterial {

    ETCHED(0, ChatFormatting.WHITE, "Etched", null, 0.5f),
    GLASS(5, ChatFormatting.GREEN, "Glass", Items.GLASS, 0.55f),
    COPPER(1, ChatFormatting.RED, "Copper", Items.COPPER_INGOT, 0.55f),
    IRON(3, ChatFormatting.GRAY, "Iron", Items.IRON_INGOT, 0.6f),
    OBSIDIAN(6, ChatFormatting.DARK_PURPLE, "Obsidian", Items.OBSIDIAN, 0.7f),
    AMETHYST(9, ChatFormatting.BLUE, "Amethyst", Items.AMETHYST_SHARD, 0.75f),
    GOLD(2, ChatFormatting.GOLD, "Gold", Items.GOLD_INGOT, 0.8f),
    DIAMOND(4, ChatFormatting.AQUA, "Diamond", Items.DIAMOND, 0.9f),
    NETHERITE(7, ChatFormatting.DARK_RED, "Netherite", Items.NETHERITE_SCRAP, 0.95f),
    ECHO(8, ChatFormatting.DARK_BLUE, "Echo", Items.ECHO_SHARD, 1f);

    private final int id;
    private final String name;
    private final ChatFormatting formatting;
    private final Item associatedItem;
    private final double efficiency;

    RuneInlayMaterial(int id, ChatFormatting formatting, String name, Item item, double efficiency) {
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        associatedItem = item;
        this.efficiency = efficiency;
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
