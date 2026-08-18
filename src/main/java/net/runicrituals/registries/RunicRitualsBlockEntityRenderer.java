package net.runicrituals.registries;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntityRenderer;

public class RunicRitualsBlockEntityRenderer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(RunicRitualsBlockEntities.RUNE_OBELISK_ENTITY_BLOCK_ENTITY, RuneObeliskEntityRenderer::new);
    }
}