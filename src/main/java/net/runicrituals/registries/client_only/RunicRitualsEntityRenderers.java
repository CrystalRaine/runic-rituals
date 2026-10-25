package net.runicrituals.registries.client_only;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.runicrituals.registries.RunicRitualsEntities;

public class RunicRitualsEntityRenderers {
    public static void registerRenderers() {
        EntityRenderers.register(RunicRitualsEntities.RITUAL_BOLT, TippableArrowRenderer::new);
    }
}
