package net.runicrituals.registries.blocks.rune_obelisk;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Objects;

public class RuneObeliskEntityRenderer implements BlockEntityRenderer<RuneObeliskEntity, RuneObeliskEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public RuneObeliskEntityRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public @NonNull RuneObeliskEntityRenderState createRenderState() {
        return new RuneObeliskEntityRenderState();
    }

    @Override
    public void submit(@NonNull RuneObeliskEntityRenderState state, @NonNull PoseStack pose, @NonNull SubmitNodeCollector queue, @NonNull CameraRenderState camera) {

        for (int slot = 0; slot < state.items.size(); slot++) {
            ItemStackRenderState itemState = state.items.get(slot);

            if (!itemState.isEmpty()) {
                pose.pushPose();

//                    these translations / rotations were annoying - why is it always in local item-space?  just why?
//                    translate to center of block
                pose.translate(new Vec3(0.5f, 1f, 0.5f));

//                    Rotate to face direction for movement
                Direction direction = Direction.from2DDataValue(slot % 4);
                float angle = direction.toYRot();
                pose.mulPose(Axis.YP.rotationDegrees(angle));

//                    Translate "Out" to edge of block
                pose.translate(new Vec3(0f, 0f, 10/32f));

//                    Scale item
                pose.scale(0.5f, 0.5f, 0.5f);

//                    render item
                itemState.submit(pose, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                pose.popPose();
            }
        }
    }

    @Override
    public void extractRenderState(@NonNull RuneObeliskEntity blockEntity, @NonNull RuneObeliskEntityRenderState state, float tickProgress, @NonNull Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.setActive(blockEntity.getActive());

        int seed = (int)blockEntity.getBlockPos().asLong();
        state.items = new ArrayList<>();

        for (int slot = 0; slot < blockEntity.getItems().size(); slot++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            ItemStack s = blockEntity.getItems().get(slot);
            ItemStack toDisplay = ItemStack.EMPTY;
            if(s.has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
                toDisplay = new ItemStack(RuneSymbol.getSymbolFromId(Objects.requireNonNull(s.get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)).runeSymbol()).getSymbolItem());
            }

            itemModelResolver.updateForTopItem(itemState, toDisplay, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + slot);
            state.items.add(itemState);
        }
    }
}
