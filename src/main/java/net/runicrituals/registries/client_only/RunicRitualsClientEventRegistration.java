package net.runicrituals.registries.client_only;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.items.BoundRunestoneItem;
import net.runicrituals.registries.items.RunicRitualsItem;

public class RunicRitualsClientEventRegistration {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player == null) return;

            for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
                if(stack.getItem() instanceof RunicRitualsItem rri) {
                    rri.clientInventoryTick(stack, (ClientLevel) player.level(), player);
                    break;
                }
            }
            
            ClientLevel clientLevel = client.level;
            if(clientLevel == null) return;
            
            for(Entity entity : clientLevel.entitiesForRendering()) {
                if(entity instanceof LivingEntity livingEntity) {
                    ItemStack heldItem = livingEntity.getMainHandItem();
                    
                    if(heldItem.getItem() instanceof RunicRitualsItem rri) {
                        rri.clientInventoryTick(heldItem, clientLevel, entity);
                    }
                    
                    ItemStack offhandItem = livingEntity.getMainHandItem();
                    
                    if(offhandItem.getItem() instanceof RunicRitualsItem rri) {
                        rri.clientInventoryTick(offhandItem, clientLevel, entity);
                    }
                }
            }
        });
    }
}
