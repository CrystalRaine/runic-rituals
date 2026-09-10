package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record RuneSymbolItemModelProperty() implements RangeSelectItemModelProperty {

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
