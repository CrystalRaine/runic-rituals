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
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class StaffItem extends RunicRitualsItem {

    public StaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        bindControl(context);
        bindPosition(context);

        return super.useOn(context);
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity livingEntity, @NonNull ItemStack itemStack, int ticksRemaining) {

        sendPosition(level, itemStack, livingEntity);
        triggerControlledBlock(level, itemStack, livingEntity, CastTrigger.USE_BOUND_ITEM);

        if(!(itemStack.has(RunicRitualsComponents.BOUND_CONTROL_POSITION) && itemStack.has(RunicRitualsComponents.BOUND_POSITION)) && livingEntity instanceof Player) {
            ((Player)livingEntity).sendOverlayMessage(Component.literal("No runes are bound to this staff"));
        }

        super.onUseTick(level, livingEntity, itemStack, ticksRemaining);
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        if(owner instanceof LivingEntity le && le.isUsingItem()) return;
        if(itemStack.has(RunicRitualsComponents.BOUND_POSITION)) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_POSITION)).getBlockPosition());
            if (rse != null) {
                rse.removeControlComponents();
            }
        }
        super.inventoryTick(itemStack, level, owner, slot);
    }

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
