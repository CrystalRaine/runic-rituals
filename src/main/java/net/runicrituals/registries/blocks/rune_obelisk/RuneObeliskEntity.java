package net.runicrituals.registries.blocks.rune_obelisk;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.blocks.RitualEntity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RuneObeliskEntity extends RitualEntity implements MenuProvider {

    static final int SLOTS_COUNT = 8;
    static final int DATA_SLOT_COUNT = 5;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(final int dataId) {
            final RuneSequence seq = RuneObeliskEntity.super.getSequence();
            return switch (dataId) {
                case 0 -> RuneObeliskEntity.super.getActive() ? 1 : 0;
                case 1 -> {
                    if (seq != null) {
                        yield (int)seq.getManaCost();
                    }
                    yield 0;
                }
                case 2 -> (int)RuneObeliskEntity.super.getMana();
                case 3 -> (int)RuneObeliskEntity.super.getManaCap();
                case 4 -> {
                    if (seq != null) {
                        yield seq.intensity;
                    }
                    yield 0;
                }
                default -> 0;
            };
        }

        @Override
        public void set(final int dataId, final int value) {
//            no setters, get data only
        }

        @Override
        public int getCount() {
            return DATA_SLOT_COUNT;
        }
    };


    public RuneObeliskEntity(BlockPos worldPosition, BlockState blockState) {
        super(RunicRitualsBlockEntities.RUNE_OBELISK_ENTITY_BLOCK_ENTITY, worldPosition, blockState, SLOTS_COUNT, 1500);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.runic-rituals.rune_obelisk");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory, @NonNull Player player) {
        return new RuneObeliskMenu(containerId, inventory, this.dataAccess, this);
    }
}
