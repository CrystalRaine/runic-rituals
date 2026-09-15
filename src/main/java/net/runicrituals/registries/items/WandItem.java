package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchorEntity;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import org.jspecify.annotations.NonNull;

public class WandItem extends RunicRitualsItem{
    public WandItem(Properties properties) {
        super(properties);
    }

    // link/unlink a ritual when this is used on it.
    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        RitualAnchorEntity rae = RitualAnchor.getBlockEntity(level, pos);
        RuneslateEntity rse = Runeslate.getBlockEntity(level, pos);
        if(rae != null && player != null) {
            if(!rae.isLinked()) {
                rae.link();
                player.sendOverlayMessage(Component.literal("Linking Ritual: Size " + rae.getRitualSize()));
            } else {
                rae.delink();
            }
        }

        if(rse != null && player != null) {
            rse.delink();
        }


        return super.useOn(context);
    }

    /* TODO: cast bound ritual if it exists */
    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        return super.use(level, player, hand);
    }

    @Override
    public void hurtEnemy(@NonNull ItemStack itemStack, @NonNull LivingEntity mob, @NonNull LivingEntity attacker) {
        super.hurtEnemy(itemStack, mob, attacker);
    }

    @Override
    public boolean mineBlock(@NonNull ItemStack itemStack, @NonNull Level level, @NonNull BlockState state, @NonNull BlockPos pos, @NonNull LivingEntity owner) {
        return super.mineBlock(itemStack, level, state, pos, owner);
    }
}
