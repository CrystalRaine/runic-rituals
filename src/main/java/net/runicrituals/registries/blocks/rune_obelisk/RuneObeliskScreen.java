package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.runicrituals.RunicRituals;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.inventory.AbstractContainerMenu.SLOTS_PER_ROW;
import static net.minecraft.world.inventory.AbstractContainerMenu.SLOT_SIZE;
import static net.runicrituals.registries.blocks.rune_engraver.RuneEngraverScreen.RUNESTONE_SLOT_SPRITE;
import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity.SLOTS_COUNT;
import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskMenu.CONTAINER_START_X;
import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskMenu.CONTAINER_START_Y;

public class RuneObeliskScreen extends AbstractContainerScreen<RuneObeliskMenu> {

    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "textures/gui/rune_obelisk.png");

    public RuneObeliskScreen(RuneObeliskMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
        int xo = this.leftPos;
        int yo = this.topPos;

        for(int i = 0; i < this.menu.getSlotCount(); i++) {
            Slot slot = this.menu.getSlot(i);
            int slotStart = CONTAINER_START_X - (SLOT_SIZE * ((Math.min(SLOTS_COUNT, SLOTS_PER_ROW)) - 1) / 2);

            if(!slot.hasItem()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RUNESTONE_SLOT_SPRITE, xo + slotStart + (i % SLOTS_PER_ROW) * SLOT_SIZE, yo + CONTAINER_START_Y + (i / 9) * SLOT_SIZE, 16, 16);
            }
        }

        String active = this.menu.getActive() ? "Active" : "Inactive";
        String cost = String.format("%1$3d", this.menu.getCost()) + "/";
        String mana = String.format("%1$3d", this.menu.getMana()) + "/";
        String manaCap = String.format("%1$3d", this.menu.getManaCap());

        Component manaStats = Component.literal("Cost/Mana/Cap | " + cost + mana + manaCap);
        Component activeText = Component.literal(active);

        graphics.text(this.font, manaStats, this.leftPos + (this.imageWidth / 2) - (this.font.width(manaStats) / 2), this.topPos + 20, -12566464, false);
        graphics.text(this.font, activeText, this.leftPos + (this.imageWidth / 2) - (this.font.width(activeText) / 2), this.topPos + 60, -12566464, false);
    }

}
