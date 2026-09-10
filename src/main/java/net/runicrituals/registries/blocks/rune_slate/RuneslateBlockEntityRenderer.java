package net.runicrituals.registries.blocks.rune_slate;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class RuneslateBlockEntityRenderer implements BlockEntityRenderer<RuneslateEntity, RuneslateEntityRenderState> {

    BlockEntityRendererProvider.Context context;

    public RuneslateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public @NonNull RuneslateEntityRenderState createRenderState() {
        return new RuneslateEntityRenderState();
    }

    @Override
    public void submit(@NonNull RuneslateEntityRenderState state, @NonNull PoseStack pose, @NonNull SubmitNodeCollector queue, @NonNull CameraRenderState camera) {

        Identifier identifier = state.getRuneSymbol().getImageIdentifier();

        pose.pushPose();

        // 1/16th to surface, plus tiny offset to avoid z-fighting
        pose.translate(new Vec3(0.5d, 1/16d + 0.0001d, 0.5d));
        pose.mulPose(Axis.YN.rotationDegrees(state.getFacing().toYRot()));

        QuadRenderer qr = new QuadRenderer();
        qr.isActive = state.getActive();
        queue.submitCustomGeometry(pose, RenderTypes.entityTranslucent(identifier), qr);

        pose.popPose();
    }

    @Override
    public void extractRenderState(@NonNull RuneslateEntity blockEntity, @NonNull RuneslateEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {

        if(!blockEntity.components().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) return;

        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.setActive(blockEntity.isLinked());
        state.setFacing(blockEntity.getFacing());

        state.setRuneSymbol(RuneSymbol.getSymbolFromId(Objects.requireNonNull(blockEntity.components().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)).runeSymbol()));
    }

    public static class QuadRenderer implements SubmitNodeCollector.CustomGeometryRenderer {
        public boolean isActive;

        @Override
        public void render(PoseStack.@NonNull Pose pose, VertexConsumer buffer) {

            float x1 = -0.5F;
            float x2 = 0.5F;
            float z1 = -0.5F;
            float z2 = 0.5F;
            float y = 0.0F;
            int light = isActive ? 0xF000F0 : 0x000000;


            buffer.addVertex(pose, x1, y, z1)
                    .setColor(255, 255, 255, 255)
                    .setUv(1.0F, 1.0F)
                    .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, -1F, 0.0F)
            ;
            buffer.addVertex(pose, x2, y, z1)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.0F, 1.0F)
                    .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, -1F, 0.0F)
            ;
            buffer.addVertex(pose, x2, y, z2)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.0F, 0.0F)
                    .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, -1F, 0.0F)
            ;
            buffer.addVertex(pose, x1, y, z2)
                    .setColor(255, 255, 255, 255)
                    .setUv(1.0F, 0.0F)
                    .setOverlay(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(0.0F, -1F, 0.0F)
            ;

        }
    }
}
