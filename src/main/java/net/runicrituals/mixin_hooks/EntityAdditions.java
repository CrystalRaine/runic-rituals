package net.runicrituals.mixin_hooks;

public interface EntityAdditions {

    default void runic_rituals$setDeltaScale(double ds){throw new IllegalStateException("Implemented via Mixin");}

    default void runic_rituals$suppressNextTick(){throw new IllegalStateException("Implemented via Mixin");}
    default void runic_rituals$resetSuppressNextTick(){throw new IllegalStateException("Implemented via Mixin");}
    default boolean runic_rituals$shouldSuppressNextTick(){throw new IllegalStateException("Implemented via Mixin");}
}
