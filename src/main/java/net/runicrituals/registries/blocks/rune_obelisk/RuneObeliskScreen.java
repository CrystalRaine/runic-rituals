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

import static net.minecraft.world.inventory.AbstractContainerMenu.SLOT_SIZE;
import static net.runicrituals.registries.blocks.rune_engraver.RuneEngraverScreen.RUNESTONE_SLOT_SPRITE;

public class RuneObeliskScreen extends AbstractContainerScreen<RuneObeliskMenu> {

    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "textures/gui/rune_obelisk_small.png");

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
            if(!slot.hasItem()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RUNESTONE_SLOT_SPRITE, xo + (i * SLOT_SIZE) + RuneObeliskMenu.CONTAINER_START_X, yo + RuneObeliskMenu.CONTAINER_START_Y, 16, 16);
            }

        }
    }
}
