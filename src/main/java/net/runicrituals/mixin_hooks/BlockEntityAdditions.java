package net.runicrituals.mixin_hooks;

public interface BlockEntityAdditions {

    default int runic_rituals$getExtraTicks() {
        throw new IllegalStateException("Implemented via Mixin");
    }
    default void runic_rituals$setExtraTicks(int value) {
        throw new IllegalStateException("Implemented via Mixin");
    }
    default void runic_rituals$resetExtraTicks() {throw new IllegalStateException("Implemented via Mixin");}

}
