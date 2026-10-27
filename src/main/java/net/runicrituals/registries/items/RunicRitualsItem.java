package net.runicrituals.registries.items;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.runicrituals.RunicRituals;
import net.runicrituals.data_generation.RunicRitualsBlockTagProvider;
import net.runicrituals.logic.runes.enums.CastTrigger;
import net.runicrituals.logic.runes.enums.RuneSymbol;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import net.runicrituals.registries.components.ActiveHoverTextComponent;
import net.runicrituals.registries.components.HoverTextComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.UUID;

import static net.runicrituals.logic.Util.vec3ToBlockPosition;
import static net.runicrituals.registries.server_only.RunicRitualsComponents.ACTIVE_ITEM;

public class RunicRitualsItem extends Item {


    public RunicRitualsItem(Properties properties) {
        super(properties);
    }

    public static class RunicRitualsItemProperties extends Item.Properties {
        public RunicRitualsItemProperties wand(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
            this.tool(material, RunicRitualsBlockTagProvider.MINEABLE_WITH_WAND, attackDamageBaseline, attackSpeedBaseline, 0.0F);
            return this;
        }

        public RunicRitualsItemProperties staff(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline) {
            this.tool(material, RunicRitualsBlockTagProvider.MINEABLE_WITH_WAND, attackDamageBaseline, attackSpeedBaseline, 0.0F);
            return this;
        }
        
        public RunicRitualsItemProperties hoverText(String textString) {
            this.component(RunicRitualsComponents.HOVER_TEXT_COMPONENT, new HoverTextComponent(textString));
            return this;
        }
        
        public RunicRitualsItemProperties activatable(String textString) {
            this.component(RunicRitualsComponents.ACTIVE_HOVER_TEXT_COMPONENT, new ActiveHoverTextComponent(textString));
            return this;
        }
    }

    public void clientInventoryTick(@NonNull ItemStack itemStack, @NonNull ClientLevel level, @NonNull Entity owner){}

    public void bindPosition(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        RuneslateEntity rse = Runeslate.getBlockEntity(level, pos);

        if(rse != null && player != null) {
            if(rse.getRuneDataComponent().runeSymbol() == RuneSymbol.BOUND.getId()) {

                context.getItemInHand().set(RunicRitualsComponents.BOUND_POSITION, rse.getBlockPos());
                player.sendOverlayMessage(Component.literal("Linking " + context.getItemInHand().getItemName().getString() + " to Position Ritual"));
            }
        }
    }

    public void toggleActive(ItemStack itemInHand) {
        itemInHand.set(ACTIVE_ITEM, !isActive(itemInHand));
    }

    public boolean isActive(ItemStack itemInHand) {
        if(itemInHand.has(ACTIVE_ITEM)) {
            return Boolean.TRUE.equals(itemInHand.get(ACTIVE_ITEM));
        }
        return false;
    }

    public void bindControl(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        RuneslateEntity rse = Runeslate.getBlockEntity(level, pos);

        if(rse != null && player != null) {
            if(rse.getRuneDataComponent().runeSymbol() == RuneSymbol.CONTROL.getId()) {

                context.getItemInHand().set(RunicRitualsComponents.BOUND_CONTROL_POSITION, rse.getBlockPos());
                player.sendOverlayMessage(Component.literal("Linking " + context.getItemInHand().getItemName().getString() + " to Control Ritual"));
            }
        }
    }

    public boolean sendPosition(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        ;
        level.getEntity(UUID.fromString(livingEntity.getStringUUID()));
        if(itemStack.has(RunicRitualsComponents.BOUND_POSITION)) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_POSITION)));
            if(rse != null) {
                rse.setComponent(RunicRitualsComponents.TARGET_ENTITY_REFERENCE, livingEntity.getStringUUID());
            } else if(livingEntity instanceof Player) {
                ((Player)livingEntity).sendOverlayMessage(Component.literal("Could not find bound position rune"));
                return false;
            }
        }
        return true;
    }

    public boolean triggerControlledBlock(Level level, ItemStack itemStack, LivingEntity livingEntity, CastTrigger triggerType){
        if(itemStack.has(RunicRitualsComponents.BOUND_CONTROL_POSITION)) {
            RuneslateEntity rse2 = Runeslate.getBlockEntity(level, Objects.requireNonNull(itemStack.get(RunicRitualsComponents.BOUND_CONTROL_POSITION)));
            if (rse2 != null && rse2.getAnchor() != null) {
                rse2.getAnchor().triggerCastingBlock(triggerType, rse2.getBlockPos());
            } else if(livingEntity instanceof Player) {
                if(rse2 == null) {
                    ((Player) livingEntity).sendOverlayMessage(Component.literal("Could not find bound control rune"));
                    return false;
                } else if (rse2.getAnchor() == null) {
                    ((Player) livingEntity).sendOverlayMessage(Component.literal("Attached rune is not part of an active ritual"));
                    return false;
                }
            }
        }
        return true;
    }
}
