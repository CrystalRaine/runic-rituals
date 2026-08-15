package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.runicrituals.registries.RunicRitualsComponents;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record RuneSymbolItemModelProperty() implements RangeSelectItemModelProperty{

    public static final MapCodec<RuneSymbolItemModelProperty> MAP_CODEC = MapCodec.unit(new RuneSymbolItemModelProperty());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        if(itemStack.getComponents().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
            return Objects.requireNonNull(itemStack.get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)).runeSymbol();
        } else {
            return -1;
        }

    }

    @Override
    public MapCodec<RuneSymbolItemModelProperty> type() {
        return MAP_CODEC;
    }
}
