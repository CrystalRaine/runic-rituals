package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.runes.enums.CastTriggers;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchorEntity;
import net.runicrituals.registries.components.BlockPositionComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

import static net.runicrituals.logic.Util.vec3ToBlockPosition;

public class StaffItem extends RunicRitualsItem {

    public StaffItem(Properties properties) {
        super(properties);
    }

    /* Bind this item to a ritual when the item is used, activate that ritual */
    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        RitualAnchorEntity rae = RitualAnchor.getBlockEntity(level, pos);
        if(rae != null && player != null) {
            if(rae.isLinked()) {

                if(context.getItemInHand().has(RunicRitualsComponents.BLOCK_POSITION_COMPONENT_DATA_COMPONENT_TYPE)) {
                    context.getItemInHand().remove(RunicRitualsComponents.BLOCK_POSITION_COMPONENT_DATA_COMPONENT_TYPE);
                }

                DataComponentPatch patch = DataComponentPatch
                    .builder()
                    .set(
                        RunicRitualsComponents.BLOCK_POSITION_COMPONENT_DATA_COMPONENT_TYPE,
                        new BlockPositionComponent(rae.getBlockPos())
                    )
                    .build();
                context.getItemInHand().applyComponents(patch);

                player.sendOverlayMessage(Component.literal("Linking Staff To Ritual"));
            }
        }

        return super.useOn(context);
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        if(!itemStack.has(RunicRitualsComponents.BLOCK_POSITION_COMPONENT_DATA_COMPONENT_TYPE)) {
            super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
            return;
        }

        RitualAnchorEntity rae = RitualAnchor.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BLOCK_POSITION_COMPONENT_DATA_COMPONENT_TYPE)).getBlockPosition());
        if(rae != null && rae.isLinked()) {
            rae.setOverridePosition(vec3ToBlockPosition(livingEntity.position()));
            RitualAnchorEntity.handleTrigger(level, CastTriggers.USE_BOUND_ITEM, rae);
        } else if(livingEntity instanceof Player){
            ((Player)livingEntity).sendOverlayMessage(Component.literal("Staff is not bound to an active ritual"));
        }
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    /* cast bound ritual if it exists */
    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack itemStack, @NonNull LivingEntity user) {
        return 7200;
    }

    @Override
    public void hurtEnemy(@NonNull ItemStack itemStack, @NonNull LivingEntity mob, @NonNull LivingEntity attacker) {
        super.hurtEnemy(itemStack, mob, attacker);
    }

    @Override
    public boolean mineBlock(@NonNull ItemStack itemStack, @NonNull Level level, @NonNull BlockState state, @NonNull BlockPos pos, @NonNull LivingEntity owner) {
        return super.mineBlock(itemStack, level, state, pos, owner);
    }
}
