package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.RunicRitualsMenuTypes;
import org.jspecify.annotations.NonNull;

public class RuneObeliskMenu extends AbstractContainerMenu {

    static final int SLOTS_COUNT = 4;
    private static final int CONTAINER_START = 0;
    private static final int INVENTORY_START = SLOTS_COUNT + 1;
    private static final int INVENTORY_END = INVENTORY_START + 27;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;
    protected static final int CONTAINER_START_X = 53;
    protected static final int CONTAINER_START_Y = 33;


    private final Container container;

    public RuneObeliskMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT));
    }

    public RuneObeliskMenu(final int containerId, final Inventory inventory, final Container container) {
        super(RunicRitualsMenuTypes.RUNE_OBELISK_MENU_TYPE, containerId);
        this.container = container;

        checkContainerSize(container, SLOTS_COUNT);
        container.startOpen(inventory.player);

        this.addSlots();
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    private void addSlots() {
        for(int i = 0; i < SLOTS_COUNT; i++) {
            Slot s = new Slot(this.container, i, CONTAINER_START_X + i * SLOT_SIZE, CONTAINER_START_Y) {
                @Override
                public boolean mayPlace(final ItemStack itemStack) {
                    return itemStack.is(RunicRitualsItems.RUNESTONE) && itemStack.has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
                }
            };
            this.addSlot(s);
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex < SLOTS_COUNT) {
            if (!this.moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, /* backwards: */ true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(stack, CONTAINER_START, SLOTS_COUNT, /* backwards: */ false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return clicked;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public int getSlotCount() {
        return this.container.getContainerSize();
    }
}
