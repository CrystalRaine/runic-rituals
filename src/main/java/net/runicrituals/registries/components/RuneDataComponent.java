package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record RuneDataComponent(int runeSymbol, int inlay) implements TooltipProvider {

    public static final Codec<RuneDataComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("rune_symbol").forGetter(RuneDataComponent::runeSymbol),
            Codec.INT.fieldOf("inlay").forGetter(RuneDataComponent::inlay)
    ).apply(builder, RuneDataComponent::new));

    public RuneDataComponent(RuneSymbol symbol, RuneInlayMaterial inlay) {
        this(symbol.getId(), inlay.getId());
    }

    public RuneDataComponent(RuneSymbol symbol, Item inlay) {
        this(symbol.getId(), RuneInlayMaterial.getByMaterial(inlay).getId());
    }

    public RuneDataComponent(int symbol, Item inlay) {
        this(symbol, RuneInlayMaterial.getByMaterial(inlay).getId());
    }

    public RuneDataComponent(int symbol, RuneInlayMaterial inlay) {
        this(symbol, inlay.getId());
    }

    public RuneDataComponent(RuneSymbol symbol, int inlay) {
        this(symbol.getId(), inlay);
    }

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, @NonNull Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        consumer.accept(Component.translatable("item.runic-rituals.element.rune_type_tooltip", RuneSymbol.getNameFromElementId(runeSymbol)).withStyle(RuneSymbol.getFormattingFromElementId(runeSymbol)));
        consumer.accept(Component.translatable("item.runic-rituals.element.rune_inlay_tooltip", RuneInlayMaterial.getNameFromElementId(inlay)).withStyle(RuneInlayMaterial.getFormattingFromElementId(inlay)));
    }

}
