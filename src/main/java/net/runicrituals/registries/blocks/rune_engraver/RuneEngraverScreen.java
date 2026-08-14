package net.runicrituals.registries.blocks.rune_engraver;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.runicrituals.RunicRituals;
import org.jspecify.annotations.NonNull;

public class RuneEngraverScreen extends AbstractContainerScreen<RuneEngraverMenu> {

    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "textures/gui/rune_engraver.png");

    public RuneEngraverScreen(RuneEngraverMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        titleLabelY = 4;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
