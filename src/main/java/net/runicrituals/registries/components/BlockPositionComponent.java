package net.runicrituals.registries.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public record BlockPositionComponent(long blockPosition) {
    public static final Codec<BlockPositionComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.LONG.fieldOf("blockPosition").forGetter(BlockPositionComponent::blockPosition)
    ).apply(builder, BlockPositionComponent::new));

    public BlockPositionComponent(BlockPos position) {
        this(position.asLong());
    }

    public BlockPos getBlockPosition() {
        return BlockPos.of(blockPosition);
    }
}
