package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public class HoverTextComponent implements TooltipProvider {

    String textId = "";
    public static final Codec<HoverTextComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("textId").forGetter(HoverTextComponent::getTextId)
    ).apply(builder, HoverTextComponent::new));

    public HoverTextComponent(String textId) {
        this.textId = textId;
    }

    public String getTextId() {
        return textId;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.translatable("item.runic-rituals." + textId + ".info").withStyle(ChatFormatting.ITALIC));
    }
}
