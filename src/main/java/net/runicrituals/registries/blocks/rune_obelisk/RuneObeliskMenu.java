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
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsItems;
import net.runicrituals.registries.RunicRitualsMenuTypes;
import net.runicrituals.registries.blocks.RitualEntity;
import org.jspecify.annotations.NonNull;

import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity.DATA_SLOT_COUNT;
import static net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity.SLOTS_COUNT;

public class RuneObeliskMenu extends AbstractContainerMenu {

    private static final int CONTAINER_START = 0;
    private static final int INVENTORY_START = SLOTS_COUNT + 1;
    private static final int INVENTORY_END = INVENTORY_START + 27;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;
    protected static final int CONTAINER_START_X = 53;
    protected static final int CONTAINER_START_Y = 33;

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
    public int getIntensity() {
        return ritualData.get(4);
    }
    public int getManaCap() {
        return ritualData.get(3);
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

    public Container getContainer() {
        return container;
    }
}
