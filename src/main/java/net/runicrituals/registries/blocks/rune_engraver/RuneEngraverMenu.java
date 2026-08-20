package net.runicrituals.registries.blocks.rune_engraver;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.*;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.*;

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
    protected static final int CONTAINER_START_X = 20;
    protected static final int CONTAINER_START_Y = 51;

    private final DataSlot selectedRuneSymbol = DataSlot.standalone();
    private SelectableRecipe.SingleInputSet<RuneEngravingRecipe> recipesForInput = SelectableRecipe.SingleInputSet.empty();
    private long lastSoundTime;
    private final Level level;
    private final ResultContainer output = new ResultContainer();
    private final ContainerLevelAccess access;

    private Runnable slotUpdateListener = () -> {};
    private final Container input = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            RuneEngraverMenu.this.slotsChanged(this);
            RuneEngraverMenu.this.slotUpdateListener.run();
        }
    };

    public RuneEngraverMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public RuneEngraverMenu(final int containerId, final Inventory inventory, ContainerLevelAccess access) {
        super(RunicRitualsMenuTypes.RUNE_ENGRAVER_MENU_MENU_TYPE, containerId);

        this.access = access;
        this.level = inventory.player.level();

        addSlots();
        addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    private void addSlots() {
        this.addSlot(new Slot(
                input,
                0,
                CONTAINER_START_X,
                CONTAINER_START_Y
        ) {
            @Override
            public boolean mayPlace(final ItemStack itemStack) {
//                only allow unengraved engravable items (runestone, runeslate)
                return itemStack.is(RuneEngravingRecipe.ENGRAVABLE_ITEMS) && !itemStack.has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
            }
        });
        this.addSlot(new Slot(
                input,
                1,
                CONTAINER_START_X,
                CONTAINER_START_Y - 2 * SLOT_SIZE
        ) {
            @Override
            public boolean mayPlace(final ItemStack itemStack) {

                return itemStack.is(RuneEngravingRecipe.INLAYABLE_ITEMS);
            }
        });

        this.addSlot(new Slot(output, 0, CONTAINER_START_X + 6 * SLOT_SIZE + 15, CONTAINER_START_Y - SLOT_SIZE) {

            @Override
            public boolean mayPlace(final ItemStack itemStack) {
                return false;
            }

            @Override
            public boolean isFake() {
                return true;
            }

            @Override
            public void onTake(Player player, ItemStack carried) {
                carried.onCraftedBy(player, carried.getCount());

                output.awardUsedRecipes(player, List.of(input.getItem(0), input.getItem(1)));

                ItemStack remaining = input.removeItem(0,1);
                input.removeItem(1,1);

                if (!remaining.isEmpty()) {
                    setupResultSlot(selectedRuneSymbol.get());
                }

                player.awardStat(RunicRitualsStats.RUNES_ENGRAVED);

                access.execute((level, pos) -> {
                    long gameTime = level.getGameTime();
                    if (lastSoundTime != gameTime) {
                        level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        lastSoundTime = gameTime;
                    }
                });
            }
        });
    }

    private void setupResultSlot(final int index) {

        Optional<RecipeHolder<RuneEngravingRecipe>> usedRecipe;

        if (!recipesForInput.isEmpty() && this.isValidRecipeIndex(index)) {
            SelectableRecipe.SingleInputEntry<RuneEngravingRecipe> entry = this.recipesForInput.entries().get(index);
            usedRecipe = entry.recipe().recipe();
        } else {
            usedRecipe = Optional.empty();
        }

        usedRecipe.ifPresentOrElse(recipe -> {
            output.setRecipeUsed(recipe);

            RuneEngravingRecipeInput recipeInput = new RuneEngravingRecipeInput(this.input.getItem(0), this.input.getItem(1));
            recipeInput.setSymbol(RuneSymbol.getSymbolFromItem(recipe.value().getResult()));

            output.setItem(0, recipe.value().assemble(recipeInput));
        }, () -> {
            output.setItem(0, ItemStack.EMPTY);
            output.setRecipeUsed(null);
        });
        this.broadcastChanges();
    }

    private boolean isValidRecipeIndex(final int buttonId) {
        return buttonId >= 0 && buttonId < this.recipesForInput.size();
    }

    @Override
    public void slotsChanged(@NonNull Container container) {
        ItemStack baseInput = this.input.getItem(0);
        int previousSelection = this.selectedRuneSymbol.get();
        SelectableRecipe.SingleInputSet<RuneEngravingRecipe> previousRecipes = this.recipesForInput;

        this.setupRecipeList(baseInput);

        if (previousSelection >= 0 && previousSelection < previousRecipes.size()) {
            SelectableRecipe.SingleInputEntry<RuneEngravingRecipe> prevEntry =
                    previousRecipes.entries().get(previousSelection);
            RuneEngravingRecipe prevRecipe = prevEntry.recipe().recipe().map(r -> r.value()).orElse(null);

            List<SelectableRecipe.SingleInputEntry<RuneEngravingRecipe>> newEntries =
                    this.recipesForInput.entries();
            for (int i = 0; i < newEntries.size(); i++) {
                Optional<RuneEngravingRecipe> newRecipeOpt =
                        newEntries.get(i).recipe().recipe().map(r -> r.value());
                if (newRecipeOpt.isPresent() && newRecipeOpt.get() == prevRecipe) {
                    this.selectedRuneSymbol.set(i);
                    this.setupResultSlot(i);
                    return;
                }
            }
        }
    }

    private void setupRecipeList(final ItemStack item) {
        selectedRuneSymbol.set(-1);
        output.setItem(0, ItemStack.EMPTY);

        if (!item.isEmpty()) {
            Collection<RecipeHolder<RuneEngravingRecipe>> allRuneEngravingRecipes = this.level.recipeAccess().getSynchronizedRecipes().getAllOfType(RunicRitualsRecipes.RUNE_ENGRAVER_RECIPE_RECIPE_TYPE);
            List<SelectableRecipe.SingleInputEntry<RuneEngravingRecipe>> runeEngravingRecipes = new ArrayList<>();

            allRuneEngravingRecipes.forEach(holder -> {
                RuneEngravingRecipe recipe = holder.value();
                if (!recipe.isSpecial() && recipe.placementInfo().isImpossibleToPlace()) {
                    RunicRituals.LOGGER.warn("Recipe {} can't be placed due to empty ingredients and will be ignored", holder.id().identifier());
                } else {
                    // choose which set of recipes to show. (etched recipes, or material recipes)
                    if(recipe.getInlayMaterial().isPresent() && !input.getSlot(1).get().isEmpty()){
                        runeEngravingRecipes.add(
                                new SelectableRecipe.SingleInputEntry<>(recipe.getRuneBase(), new SelectableRecipe<>(recipe.resultDisplay(), Optional.of(holder)))
                        );
                    } else if(recipe.getInlayMaterial().isEmpty() && input.getSlot(1).get().isEmpty()) {
                        runeEngravingRecipes.add(
                                new SelectableRecipe.SingleInputEntry<>(recipe.getRuneBase(), new SelectableRecipe<>(recipe.resultDisplay(), Optional.of(holder)))
                        );
                    }
                }
            });

            this.recipesForInput = new SelectableRecipe.SingleInputSet<>(runeEngravingRecipes);
        } else {
            this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
        }
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return stillValid(this.access, player, RunicRitualsBlocks.RUNE_ENGRAVER);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(Player player, int slotIndex) {
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
    public boolean clickMenuButton(final Player player, final int buttonId) {
        if (this.selectedRuneSymbol.get() == buttonId) {
            return false;
        }

        if (this.isValidRecipeIndex(buttonId)) {
            this.selectedRuneSymbol.set(buttonId);
            this.setupResultSlot(buttonId);
        }

        return true;
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

    public int getNumberOfVisibleRecipes() {
        return this.recipesForInput.size();
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRuneSymbol.get();
    }

    public SelectableRecipe.SingleInputSet<RuneEngravingRecipe> getVisibleRecipes() {
        return this.recipesForInput;
    }

    public boolean hasInputItem() {
        return !this.input.getSlot(0).get().isEmpty() && !this.recipesForInput.isEmpty();
    }

    public void registerUpdateListener(final Runnable slotUpdateListener) {
        this.slotUpdateListener = slotUpdateListener;
    }

    public SlotAccess getInlayMaterialSlot() {
        return input.getSlot(1);
    }


    public SlotAccess getRuneBaseSlot() {
        return input.getSlot(0);
    }
}
