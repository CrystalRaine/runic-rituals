package net.runicrituals.registries;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntityRenderer;

public class RunicRitualsBlockEntityRenderer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(RunicRitualsBlockEntities.RUNESLATE_BLOCK_ENTITY, RuneslateEntityRenderer::new);
    }
}