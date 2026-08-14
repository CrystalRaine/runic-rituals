package net.runicrituals.item.blocks.rune_engraver;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record RuneEngravingRecipeInput(ItemStack runeBase, ItemStack inlayMaterial) implements RecipeInput {

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
