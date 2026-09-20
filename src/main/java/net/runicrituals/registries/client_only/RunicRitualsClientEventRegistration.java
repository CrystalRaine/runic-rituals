package net.runicrituals.registries.client_only;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.items.BoundRunestoneItem;

public class RunicRitualsClientEventRegistration {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player == null) return;

            for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
                if (stack.is(RunicRitualsItems.BOUND_RUNESTONE)) {
                    ((BoundRunestoneItem)stack.getItem()).clientInventoryTick(stack, player.level(), player);
                    break;
                }
            }
        });
    }
}
