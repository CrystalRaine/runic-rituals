package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.runicrituals.logic.ManaStorage;
import org.jspecify.annotations.NonNull;

public class ManameterItem extends RunicRitualsItem {

    public ManameterItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        BlockEntity rae = level.getBlockEntity(pos);
        if(rae instanceof ManaStorage msb && player != null && !level.isClientSide()) {
            player.sendOverlayMessage(Component.literal("Mana in ritual: " + String.format("%.3f", (msb.getMana().getMana()))));
        }
        return super.useOn(context);
    }

}
