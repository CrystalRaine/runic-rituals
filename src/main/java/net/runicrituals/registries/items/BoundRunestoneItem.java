package net.runicrituals.registries.items;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BoundRunestoneItem extends RunicRitualsItem{
    public BoundRunestoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        bindPosition(context);
        return super.useOn(context);
    }

    public @NonNull InteractionResult use(final @NonNull Level level, final @NonNull Player player, final @NonNull InteractionHand hand) {

        this.toggleActive(player.getItemInHand(hand));
//        super fancy translation, should turn into Item Name: [Active/Inactive]
        player.sendOverlayMessage(Component.literal(player.getItemInHand(hand).getComponents().getOrDefault(DataComponents.CUSTOM_NAME, Component.translatable("item.runic-rituals.bound_runestone")).getString() + ": " + (this.isActive(player.getItemInHand(hand)) ? Component.translatable("status.runic-rituals.active") : Component.translatable("status.runic-rituals.inactive")).getString()));
        return super.use(level, player, hand);
    }


    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        if(!(owner instanceof LivingEntity)) return;
        if(!isActive(itemStack)) return;
        if(!sendPosition(level, itemStack, owner.asLivingEntity())){
            toggleActive(itemStack);
        }
        super.inventoryTick(itemStack, level, owner, slot);
    }

    @Override
    public void clientInventoryTick(@NonNull ItemStack itemStack, @NonNull ClientLevel level, @NonNull Entity owner) {
        if(!(owner instanceof LivingEntity)) return;
        if(!isActive(itemStack)) return;
        if(!sendPosition(level, itemStack, owner.asLivingEntity())){
            toggleActive(itemStack);
        }
    }
    
    public void hoverText() {
    
    }
}
