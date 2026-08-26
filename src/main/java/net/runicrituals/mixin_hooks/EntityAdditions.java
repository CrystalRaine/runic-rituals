package net.runicrituals.mixin_hooks;

public interface EntityAdditions {

    default void runic_rituals$setDeltaScale(double ds, double unixTimeMovementChangeEnds){throw new IllegalStateException("Implemented via Mixin");}
    default double runic_rituals$getDeltaScale(){throw new IllegalStateException("Implemented via Mixin");}

}
