package net.runicrituals.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.components.RuneDataComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin {

    @Inject(
            method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemBar(Lnet/minecraft/world/item/ItemStack;II)V"
            )
    )
    public void addDecoration(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {
        if(itemStack.getComponents().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
            RuneDataComponent component = itemStack.getComponents().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
            Identifier sprite = RuneSymbol.getSymbolFromId(component.runeSymbol()).getImageIdentifier();
            GuiGraphicsExtractor extractor = (GuiGraphicsExtractor) (Object)this;

            if(itemStack.is(RunicRitualsBlocks.RUNESLATE.asItem())) {
                extractor.blit(
                        RenderPipelines.GUI_TEXTURED,
                        sprite,
                        x,
                        y,
                        0f,
                        0f,
                        16,
                        16,
                        16,
                        16
                );
            }
        }
    }

    @Inject(method = "fakeItem(Lnet/minecraft/world/item/ItemStack;III)V", at=@At("TAIL"))
    public void renderSymbolForRecipe(ItemStack itemStack, int x, int y, int seed, CallbackInfo ci) {
        if (!itemStack.isEmpty()) {
            if (itemStack.getComponents().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
                RuneDataComponent component = itemStack.getComponents().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
                Identifier sprite = RuneSymbol.getSymbolFromId(component.runeSymbol()).getImageIdentifier();
                GuiGraphicsExtractor extractor = (GuiGraphicsExtractor) (Object) this;

                if (itemStack.is(RunicRitualsBlocks.RUNESLATE.asItem())) {
                    extractor.blit(
                            RenderPipelines.GUI_TEXTURED,
                            sprite,
                            x,
                            y,
                            0f,
                            0f,
                            16,
                            16,
                            16,
                            16
                    );
                }
            }
        }
    }
}
