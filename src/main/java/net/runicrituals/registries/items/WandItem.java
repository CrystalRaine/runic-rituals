package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;

public class WandItem extends RunicRitualsItem{
    public WandItem(Properties properties) {
        super(properties);
    }

    // link/unlink a ritual when this is used on it.
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        RuneslateEntity rse = Runeslate.getBlockEntity(level, pos);

        if(rse != null && player != null) {
            if(!rse.isLinked()) {
                rse.isAnchor = true;
                rse.setAnchor(rse);

                rse.link();
                if(rse.getChainIndex() == 0) {
                    rse.getAnchor().delink();
                    player.sendOverlayMessage(Component.literal("Linking Ritual: Failed due to incomplete loop"));
                    return InteractionResult.SUCCESS;
                }

                boolean validBase = rse.getBase();
                if(!validBase) {
                    rse.getAnchor().delink();
                    player.sendOverlayMessage(Component.literal("Linking Ritual: Failed due to being unable to validate contained volume"));
                    return InteractionResult.SUCCESS;
                }

                if(level.isClientSide()) {
                    player.sendOverlayMessage(Component.literal("Completed Ritual Circle, Size: " + (rse.getChainIndex())));
                }
            } else if(rse.getAnchor() != null){
                rse.getAnchor().delink();
            }
        }
        return super.useOn(context);
    }

    // TODO: cast bound ritual
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }
}
