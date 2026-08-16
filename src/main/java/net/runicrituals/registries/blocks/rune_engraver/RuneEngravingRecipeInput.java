package net.runicrituals.registries.blocks.rune_engraver;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.runicrituals.logic.RuneSymbol;

public class RuneEngravingRecipeInput implements RecipeInput {

    private RuneSymbol symbol;
    private final ItemStack runeBase;
    private final ItemStack inlayMaterial;

    public RuneEngravingRecipeInput(ItemStack runeBase, ItemStack inlayMaterial) {
        this.runeBase = runeBase;
        this.inlayMaterial = inlayMaterial;
    }

    public void setSymbol(RuneSymbol symbol) {
        this.symbol = symbol;
    }

    public RuneSymbol getSymbol() {
        return symbol;
    }

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> this.runeBase;
            case 1 -> this.inlayMaterial;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
