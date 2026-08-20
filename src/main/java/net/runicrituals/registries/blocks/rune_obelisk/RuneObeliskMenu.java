package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.RunicRitualsMenuTypes;
import org.jspecify.annotations.NonNull;

import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity.DATA_SLOT_COUNT;
import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity.SLOTS_COUNT;

public class RuneObeliskMenu extends AbstractContainerMenu {

    private static final int CONTAINER_START = 0;
    private static final int INVENTORY_START = SLOTS_COUNT + 1;
    private static final int INVENTORY_END = INVENTORY_START + 27;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;
    protected static final int CONTAINER_START_X = 80;
    protected static final int CONTAINER_START_Y = 33;
    private static final int SLOTS_PER_ROW = 9;

    private final Container container;
    private final ContainerData ritualData;

    public RuneObeliskMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainerData(DATA_SLOT_COUNT), new SimpleContainer(SLOTS_COUNT));
    }

    public RuneObeliskMenu(final int containerId, final Inventory inventory, final ContainerData ritualData, final Container container) {
        super(RunicRitualsMenuTypes.RUNE_OBELISK_MENU_TYPE, containerId);
        this.container = container;
        this.ritualData = ritualData;

        checkContainerSize(container, SLOTS_COUNT);
        container.startOpen(inventory.player);

        checkContainerDataCount(ritualData, DATA_SLOT_COUNT);
        this.addDataSlots(ritualData);

        this.addSlots();
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    public boolean getActive() {
        return ritualData.get(0) == 1;
    }
    public int getMana() {
        return ritualData.get(2);
    }
    public int getCost() {
        return ritualData.get(1);
    }
    public int getManaCap() {
        return ritualData.get(3);
    }

    private void addSlots() {
        int slotStart = CONTAINER_START_X - (SLOT_SIZE * ((Math.min(SLOTS_COUNT, SLOTS_PER_ROW)) - 1) / 2);

        for(int i = 0; i < SLOTS_COUNT; i++) {
            Slot s = new Slot(this.container, i, slotStart + (i % SLOTS_PER_ROW) * SLOT_SIZE, CONTAINER_START_Y + (i / 9) * SLOT_SIZE) {
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

    public Container getContainer() {
        return container;
    }
}
