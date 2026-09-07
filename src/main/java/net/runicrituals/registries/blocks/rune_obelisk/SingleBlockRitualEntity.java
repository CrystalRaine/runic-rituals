package net.runicrituals.registries.blocks.rune_obelisk;

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
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SingleBlockRitualEntity extends BlockEntity implements Container {

    private final NonNullList<ItemStack> items;
    boolean active;
    private double mana;
    private final double manaCap;
    private RuneObeliskRuneSequence sequence;

    // cache block -> block costs so that client can read them.
    private final Map<Integer, Double> blockCostMap = new HashMap<>();
    private boolean blockCacheDirty = false;

    public SingleBlockRitualEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, int runeSlots, double manaCap) {
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
    public RuneObeliskRuneSequence getSequence() {
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

    public static void tick(Level level, BlockPos pos, BlockState state, SingleBlockRitualEntity blockEntity) {
        if(level.getBlockEntity(pos) instanceof SingleBlockRitualEntity singleBlockRitualEntity) {
            if(singleBlockRitualEntity.sequence == null) {
                singleBlockRitualEntity.sequence = new RuneObeliskRuneSequence(singleBlockRitualEntity, level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            }
            singleBlockRitualEntity.sequence.clearRunes();

            getSequence(singleBlockRitualEntity.sequence, singleBlockRitualEntity.items);

            if(singleBlockRitualEntity.active) {
                singleBlockRitualEntity.sequence.tick();
            }
        }
    }

    public static void getSequence(RuneObeliskRuneSequence sequence, NonNullList<ItemStack> items){
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

        saveBlockCostMap(output);
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        setActive(input.getBooleanOr("active", false));
        mana = input.getDoubleOr("mana", 0);

        // This one line... is not in the goddamn docs on block containers and is required for the entity renderer
        // to work properly here. https://docs.fabricmc.net/26.1.2/develop/blocks/block-containers
        items.clear();

        loadBlockCostMap(input);
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

    public Double getCachedCostForBlock(Integer blockId) {
        return blockCostMap.get(blockId);
    }

    public void cacheBlockCost(Integer blockId, Double cost) {
        Double old = blockCostMap.get(blockId);
        if(old == null || !old.equals(cost)) {
            blockCacheDirty = true;
        }

        blockCostMap.put(blockId, cost);
    }

    private void saveBlockCostMap(@NonNull ValueOutput output) {
        output.putInt("blockCount", blockCostMap.size());

        for(int i = 0; i < blockCostMap.size(); i++) {
            output.putDouble("block[" + i + "]", blockCostMap.get(i));
        }

        blockCacheDirty = false;
    }

    private void loadBlockCostMap(@NonNull ValueInput input) {
        int blockCount = input.getIntOr("blockCount", 0);

        for(int i = 0; i < blockCount; i++) {
            Double blockVal = input.getDoubleOr("block[" + i + "]", 0);
            blockCostMap.put(i, blockVal);
        }
    }

    public boolean isBlockCostCacheDirty() {
        return blockCacheDirty;
    }
}
