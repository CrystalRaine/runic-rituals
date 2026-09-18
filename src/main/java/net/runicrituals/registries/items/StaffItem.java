package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.runes.enums.RuneSymbol;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import net.runicrituals.registries.components.BlockPositionComponent;
import net.runicrituals.registries.components.ControlRuneStateComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

        RuneslateEntity rse = Runeslate.getBlockEntity(level, pos);
        if(rse != null && player != null) {
            if(rse.isLinked() && rse.getRuneDataComponent().runeSymbol() == RuneSymbol.BOUND.getId()) {

                context.getItemInHand().set(RunicRitualsComponents.BOUND_POSITION, new BlockPositionComponent(rse.getBlockPos()));
                player.sendOverlayMessage(Component.literal("Linking Staff To Position Ritual"));
            }
            if(rse.isLinked() && rse.getRuneDataComponent().runeSymbol() == RuneSymbol.CONTROL.getId()) {

                context.getItemInHand().set(RunicRitualsComponents.BOUND_CONTROL_POSITION, new BlockPositionComponent(rse.getBlockPos()));
                player.sendOverlayMessage(Component.literal("Linking Staff to Control Ritual"));
            }
        }

        return super.useOn(context);
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {

        if(itemStack.has(RunicRitualsComponents.BOUND_POSITION)) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_POSITION)).getBlockPosition());
            if(rse != null) {
                rse.setComponent(RunicRitualsComponents.BOUND_POSITION, new BlockPositionComponent(vec3ToBlockPosition(livingEntity.position())));
            }
            super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
            return;
        } else if(itemStack.has(RunicRitualsComponents.BOUND_CONTROL_POSITION)) {
            RuneslateEntity rse2 = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_CONTROL_POSITION)).getBlockPosition());
            if (rse2 != null) {
                rse2.setComponent(RunicRitualsComponents.CONTROL_RUNE_STATE_COMPONENT, new ControlRuneStateComponent(true));
            }
        } else if(livingEntity instanceof Player){
            ((Player)livingEntity).sendOverlayMessage(Component.literal("Staff is not bound to an active rune"));
        }
        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if(owner instanceof LivingEntity le && le.isUsingItem()) return;
        if(itemStack.has(RunicRitualsComponents.BOUND_POSITION)) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_POSITION)).getBlockPosition());
            if (rse != null) {
                rse.removeControlComponents();
            }
        }
        if(itemStack.has(RunicRitualsComponents.BOUND_CONTROL_POSITION)) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_CONTROL_POSITION)).getBlockPosition());
            if (rse != null) {
                rse.removeControlComponents();
            }
        }
        super.inventoryTick(itemStack, level, owner, slot);
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
