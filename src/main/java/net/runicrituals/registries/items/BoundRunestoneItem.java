package net.runicrituals.registries.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, @Nullable EquipmentSlot slot) {
        if(!(owner instanceof LivingEntity)) return;
        sendPosition(level, itemStack, owner.asLivingEntity());
        super.inventoryTick(itemStack, level, owner, slot);
    }

    @Override
    public void clientInventoryTick(@NonNull ItemStack itemStack, @NonNull Level level, @NonNull Entity owner) {
        if(!(owner instanceof LivingEntity)) return;
        sendPosition(level, itemStack, owner.asLivingEntity());
    }

}
