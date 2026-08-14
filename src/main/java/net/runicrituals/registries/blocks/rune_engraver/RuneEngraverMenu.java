package net.runicrituals.registries.blocks.rune_engraver;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.runicrituals.registries.RunicRitualsBlocks;
import net.runicrituals.registries.RunicRitualsComponents;
import net.runicrituals.registries.RunicRitualsMenuTypes;
import net.runicrituals.registries.RunicRitualsRecipes;
import net.runicrituals.registries.components.RuneDataComponent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class RuneEngraverMenu extends AbstractContainerMenu {

    private static final int INPUT_SLOTS_COUNT = 2;
    private static final int RESULT_SLOT = 2;
    private static final int INPUT_SLOTS_START = 0;
    private static final int INPUT_SLOTS_END = INPUT_SLOTS_START + INPUT_SLOTS_COUNT;
    private static final int INVENTORY_START = RESULT_SLOT + 1;
    private static final int INVENTORY_END = INVENTORY_START + 27; // 30
    private static final int HOTBAR_START = INVENTORY_END; // 30
    private static final int HOTBAR_END = HOTBAR_START + 9; // 39

    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;

    private static final int CONTAINER_START_X = 20;
    private static final int CONTAINER_START_Y = 51;

    private final ResultContainer output = new ResultContainer();
    private final ContainerLevelAccess access;

    private final Container input = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            RuneEngraverMenu.this.slotsChanged(this);
        }
    };

    @Nullable
    private final Player player;

    public RuneEngraverMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public RuneEngraverMenu(final int containerId, final Inventory inventory, ContainerLevelAccess access) {
        super(RunicRitualsMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, containerId);

        this.access = access;
        this.player = inventory.player;

        addSlots();

        addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);

    }

    private void addSlots() {
        this.addSlot(new Slot(
                input,
                0,
                CONTAINER_START_X,
                CONTAINER_START_Y
        ));
        this.addSlot(new Slot(
                input,
                1,
                CONTAINER_START_X,
                CONTAINER_START_Y - 2 * SLOT_SIZE
        ));
        this.addSlot(new RuneEngraverResultSlot(
                this,
                output,
                0,
                CONTAINER_START_X + 6 * SLOT_SIZE + 15,
                CONTAINER_START_Y - 18
        ));

    }

    protected void onTake(final Player player, final ItemStack stack) {
        stack.onCraftedBy(player, stack.getCount());
        output.awardUsedRecipes(player, List.of(input.getItem(0), input.getItem(1)));
        input.removeItem(0,1);
        input.removeItem(1,1);
    }

    @Override
    public void slotsChanged(@NonNull Container container) {
        super.slotsChanged(container);

        access.execute((level, blockPos) -> {
            if (level instanceof ServerLevel serverLevel && container == this.input) {
                RuneEngravingRecipeInput recipeInput = new RuneEngravingRecipeInput(this.input.getItem(0), this.input.getItem(1));
                Optional<RecipeHolder<RuneEngravingRecipe>> maybeRecipe = serverLevel.recipeAccess().getRecipeFor(RunicRitualsRecipes.RUNE_ENGRAVER_RECIPE_RECIPE_TYPE, recipeInput, serverLevel);
                ItemStack result = ItemStack.EMPTY;

                if (maybeRecipe.isPresent()) {
                    RecipeHolder<RuneEngravingRecipe> recipeHolder = maybeRecipe.get();
                    RuneEngravingRecipe recipe = recipeHolder.value();

                    if (this.output.setRecipeUsed((ServerPlayer) this.player, recipeHolder)) {
                        ItemStack recipeResult = recipe.assemble(recipeInput);

                        // add engraving Component
                        recipeResult.set(RunicRitualsComponents.ELEMENT_DATA_COMPONENT_TYPE, new RuneDataComponent(0,this.input.getItem(1).getItem()));

                        if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                            result = recipeResult;
                        }
                    }
                }

                this.output.setItem(0, result);
                this.setRemoteSlot(0, result);

                assert this.player != null;
                ((ServerPlayer) this.player).connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 2, result));
            }
        });
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return stillValid(this.access, player, RunicRitualsBlocks.RUNE_ENGRAVER);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        //noinspection ConstantValue
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex == RESULT_SLOT) {
            stack.getItem().onCraftedBy(stack, player);

            if (!this.moveItemStackTo(stack, INVENTORY_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, clicked);
        } else if (slotIndex >= INVENTORY_START && slotIndex < HOTBAR_END) {
            if (!this.moveItemStackTo(stack, INPUT_SLOTS_START, INPUT_SLOTS_END, false)) {
                if (slotIndex < HOTBAR_START) {
                    if (!this.moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (!this.moveItemStackTo(stack, INVENTORY_START, HOTBAR_END, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == clicked.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        if (slotIndex == RESULT_SLOT) {
            player.drop(stack, false);
        }

        return clicked;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.input));
    }

    @Override
    public boolean canTakeItemForPickAll(final @NonNull ItemStack carried, final Slot target) {
        return target.container != this.output && super.canTakeItemForPickAll(carried, target);
    }
}
