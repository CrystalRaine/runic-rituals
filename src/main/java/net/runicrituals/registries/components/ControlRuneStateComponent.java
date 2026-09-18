package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ControlRuneStateComponent(boolean active) {
    public static final Codec<ControlRuneStateComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.fieldOf("control").forGetter(ControlRuneStateComponent::active)
    ).apply(builder, ControlRuneStateComponent::new));
}
