package net.runicrituals.registries.blocks.rune_engraver;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record RuneEngraverRecipeDisplay(SlotDisplay input, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {

    public static final MapCodec<RuneEngraverRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            SlotDisplay.CODEC.fieldOf("input").forGetter(RuneEngraverRecipeDisplay::input),
                            SlotDisplay.CODEC.fieldOf("result").forGetter(RuneEngraverRecipeDisplay::result),
                            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(RuneEngraverRecipeDisplay::craftingStation)
                    )
                    .apply(i, RuneEngraverRecipeDisplay::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RuneEngraverRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            RuneEngraverRecipeDisplay::input,
            SlotDisplay.STREAM_CODEC,
            RuneEngraverRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC,
            RuneEngraverRecipeDisplay::craftingStation,
            RuneEngraverRecipeDisplay::new
    );

    public static final RecipeDisplay.Type<RuneEngraverRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public Type<? extends RecipeDisplay> type() {
        return TYPE;
    }
}
