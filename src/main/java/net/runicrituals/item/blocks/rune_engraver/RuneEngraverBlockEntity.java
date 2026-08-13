package net.runicrituals.item.blocks.rune_engraver;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.item.ModBlockEntities;
import net.runicrituals.item.ModItems;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static net.minecraft.world.item.Items.*;

public class RuneEngraverBlockEntity extends BlockEntity implements Container, WorldlyContainer, MenuProvider {
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2};

    NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

    public RuneEngraverBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.RUNE_ENGRAVER_BLOCK_ENTITY, worldPosition, blockState);
    }

//    worldly container

    @Override
    public int @NonNull [] getSlotsForFace(@NonNull Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack itemStack, @Nullable Direction direction) {
        if (slot == 0 && (itemStack.is(ModItems.RUNESTONE) || itemStack.is(ModItems.RUNESLATE))) {
            return true;
        }
        if (slot == 1 && (
               itemStack.is(COPPER_INGOT)
            || itemStack.is(IRON_INGOT)
            || itemStack.is(GOLD_INGOT)
            || itemStack.is(DIAMOND)
            || itemStack.is(GLASS)
            || itemStack.is(OBSIDIAN)
        )) {
            return true;
        }
        if(slot == 2) {
            return false;
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NonNull ItemStack itemStack, @NonNull Direction direction) {
        if(slot != 2) {
            return false;
        }
        return direction == Direction.DOWN;
    }

//    container implementation
    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return stacks.getFirst().isEmpty() && stacks.get(1).isEmpty() && stacks.get(2).isEmpty();
    }

    @Override
    public @NonNull ItemStack getItem(int slot) {
        return stacks.get(slot);
    }

    @Override
    public @NonNull ItemStack removeItem(int slot, int count) {
        ItemStack removed = ContainerHelper.removeItem(stacks, slot, count);
        if(!removed.isEmpty()) {
            setChanged();
        }
        return removed;
    }

    @Override
    public @NonNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(stacks, slot);
    }

    @Override
    public void setItem(int slot, @NonNull ItemStack itemStack) {
        stacks.set(slot, itemStack);
        stacks.get(slot).limitSize(getMaxStackSize(stacks.get(slot)));
        setChanged();
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        stacks.clear();
    }

    @Override
    public void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.stacks);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.stacks);
        super.saveAdditional(output);
    }

//    MenuProvider
    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.runic-rituals.rune_engraver");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory, @NonNull Player player) {
        return new RuneEngraverMenu(containerId, inventory, this);
    }
}
