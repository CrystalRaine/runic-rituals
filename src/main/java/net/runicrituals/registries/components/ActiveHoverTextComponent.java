package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.runicrituals.RunicRituals;
import org.apache.commons.compress.harmony.pack200.RunCodec;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

import static net.runicrituals.registries.server_only.RunicRitualsComponents.ACTIVE_ITEM;

public class ActiveHoverTextComponent implements TooltipProvider {

    String textId;
    public static final Codec<ActiveHoverTextComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("textId").forGetter(ActiveHoverTextComponent::getTextId)
    ).apply(builder, ActiveHoverTextComponent::new));

    public ActiveHoverTextComponent(String textId) {
        this.textId = textId;
    }

    public String getTextId() {
        return textId;
    }

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        consumer.accept(
            Component.translatable("item.runic-rituals.active_" + textId + ".info", components.getOrDefault(ACTIVE_ITEM, false) ? Component.translatable("status.runic-rituals.active") : Component.translatable("status.runic-rituals.inactive"))
                .withStyle(Boolean.TRUE.equals(components.get(ACTIVE_ITEM)) ? ChatFormatting.GREEN : ChatFormatting.RED)
        );
    }
}
