package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RuneObeliskEntity extends BlockEntity implements Container, MenuProvider {

    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private boolean active;

    public RuneObeliskEntity(BlockPos worldPosition, BlockState blockState) {
        super(RunicRitualsBlockEntities.RUNE_OBELISK_ENTITY_BLOCK_ENTITY, worldPosition, blockState);
    }

    protected void toggleActive() {
        this.active = !getActive();

        this.setChanged();
    }

    protected boolean getActive() {
        return active;
    }

//    Data save / load
    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.items);
        output.putBoolean("active", getActive());

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        active = input.getBooleanOr("active", true);
        ContainerHelper.loadAllItems(input, this.items);
    }

//    Data sync
    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

//    Container / menu implementations
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NonNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NonNull ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(items, slot, count);

        if (!result.isEmpty()) {
            this.setChanged();
        }

        return result;
    }

    @Override
    public @NonNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, @NonNull ItemStack itemStack) {
        items.set(slot, itemStack);

        itemStack.limitSize(this.getMaxStackSize(itemStack));

        this.setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.runic-rituals.rune_obelisk");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory, @NonNull Player player) {
        return new RuneObeliskMenu(containerId, inventory, this);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
