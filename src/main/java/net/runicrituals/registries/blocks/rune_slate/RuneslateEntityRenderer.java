package net.runicrituals.registries.blocks.rune_slate;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntityRenderState;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Objects;

import static net.runicrituals.registries.blocks.rune_slate.Runeslate.FACING;

public class RuneslateEntityRenderer implements BlockEntityRenderer<RuneslateEntity, RuneslateEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public RuneslateEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public @NonNull RuneslateEntityRenderState createRenderState() {
        return new RuneslateEntityRenderState();
    }

    @Override
    public void submit(@NonNull RuneslateEntityRenderState state, @NonNull PoseStack pose, @NonNull SubmitNodeCollector queue, @NonNull CameraRenderState camera) {

        ItemStackRenderState itemState = state.getRuneSymbol();
        pose.pushPose();

        pose.mulPose(Axis.XP.rotationDegrees(90));
        pose.translate(new Vec3(0.5f, 0.5f, -0.05f));
        pose.mulPose(Axis.ZP.rotationDegrees(state.getFacing().toYRot()));

        itemState.submit(pose, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }

    @Override
    public void extractRenderState(@NonNull RuneslateEntity blockEntity, @NonNull RuneslateEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {

        if(!blockEntity.components().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) return;

        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.setActive(blockEntity.isLinked());
        state.setFacing(blockEntity.getFacing());

        int seed = (int)blockEntity.getBlockPos().asLong();

        ItemStackRenderState itemState = new ItemStackRenderState();
        ItemStack s = RuneSymbol.getSymbolFromId(Objects.requireNonNull(blockEntity.components().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)).runeSymbol()).getSymbolItem().getDefaultInstance();

        itemModelResolver.updateForTopItem(itemState, s, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed);
        state.setRuneSymbol(itemState);
    }
}
