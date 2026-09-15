package net.runicrituals.registries.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchorEntity;
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

        RitualAnchorEntity rae = RitualAnchor.getBlockEntity(level, pos);
        if(rae != null && player != null && !level.isClientSide()) {
            if(rae.isLinked()) {
                player.sendOverlayMessage(Component.literal("Mana in ritual: " + String.format("%.3f", (rae.getMana().getMana()))));
            }
        }
        return super.useOn(context);
    }

}
