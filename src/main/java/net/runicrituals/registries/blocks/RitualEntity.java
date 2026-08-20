package net.runicrituals.registries.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class RitualEntity extends BlockEntity implements Container {

    private final NonNullList<ItemStack> items;
    boolean active;
    private double mana;
    private final double manaCap;
    private RuneSequence sequence;

    public RitualEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, int runeSlots, double manaCap) {
        super(type, worldPosition, blockState);
        items = NonNullList.withSize(runeSlots, ItemStack.EMPTY);
        this.manaCap = manaCap;
    }

    public double getMana() {
        return mana;
    }
    public double getManaCap() {
        return manaCap;
    }
    public RuneSequence getSequence() {
        return sequence;
    }
    public boolean getActive() {
        return active;
    }
    public void toggleActive() {
        this.active = !getActive();

        this.setChanged();
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void addMana(double amount) {
        mana = Math.clamp(mana + amount, 0, manaCap);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RitualEntity blockEntity) {
        if(level.getBlockEntity(pos) instanceof RitualEntity ritualEntity) {
            if(ritualEntity.sequence == null) {
                ritualEntity.sequence = new RuneSequence(ritualEntity, level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            }
            ritualEntity.sequence.clearRunes();

            getSequence(ritualEntity.sequence, ritualEntity.items);

            if(ritualEntity.sequence.getManaCost() <= ritualEntity.mana) {
                if(ritualEntity.active) {
                    ritualEntity.sequence.tick();
                }
            }
        }
    }

    public static void getSequence(RuneSequence sequence, NonNullList<ItemStack> items){
        sequence.clearRunes();
        for (ItemStack stack : items) {
            if (stack.has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
                RuneDataComponent comp = stack.get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
                assert comp != null;
                sequence.addRune(RuneSymbol.getSymbolFromId(comp.runeSymbol()), RuneInlayMaterial.getElementFromId(comp.inlay()));
            }
        }
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
        output.putBoolean("active", active);
        output.putDouble("mana", mana);

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        setActive(input.getBooleanOr("active", true));
        mana = input.getDoubleOr("mana", 0);

        // This one line... is not in the goddamn docs on block containers and is required for the entity renderer
        // to work properly here. https://docs.fabricmc.net/26.1.2/develop/blocks/block-containers
        items.clear();

        ContainerHelper.loadAllItems(input, items);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public @NonNull ItemStack removeItem(int slot, int count) {
        ItemStack removedItems = ContainerHelper.removeItem(items, slot, count);

        if (!removedItems.isEmpty()) {
            this.setChanged();
        }

        return removedItems;
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
        level.gameEvent(GameEvent.BLOCK_CHANGE, worldPosition, GameEvent.Context.of(state));
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }


    public List<ItemStack> getItems() {
        return items;
    }
}
