package net.runicrituals.item.blocks.rune_engraver;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.item.ModItems;
import net.runicrituals.item.ModMenuTypes;
import org.jspecify.annotations.NonNull;

public class RuneEngraverMenu extends AbstractContainerMenu {

    private final Container container;

    public static final int SLOTS_COUNT = 3;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;

    private static final int CONTAINER_START_X = 20;
    private static final int CONTAINER_START_Y = 51;

    private static final int INVENTORY_START = SLOTS_COUNT;
    private static final int INVENTORY_END = INVENTORY_START + Inventory.INVENTORY_SIZE;

    public RuneEngraverMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT));

    }

    public RuneEngraverMenu(final int containerId, final Inventory inventory, final Container container) {
        super(ModMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, containerId);
        checkContainerSize(container, SLOTS_COUNT);
        this.container = container;

        container.startOpen(inventory.player);

        addSlots();

        addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    private void addSlots() {
        this.addSlot(new Slot(
                this.container,
                0,
                CONTAINER_START_X,
                CONTAINER_START_Y
        ));
        this.addSlot(new Slot(
                this.container,
                1,
                CONTAINER_START_X,
                CONTAINER_START_Y - 2 * SLOT_SIZE
        ));
        this.addSlot(new Slot(
                this.container,
                2,
                CONTAINER_START_X + 6 * SLOT_SIZE + 15,
                CONTAINER_START_Y - 18
        ));
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        Slot slot = slots.get(slotIndex);

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
            if (!this.moveItemStackTo(stack, 0, SLOTS_COUNT, /* backwards: */ false)) {
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
        return container.stillValid(player);
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
