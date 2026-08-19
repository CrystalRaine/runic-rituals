package net.runicrituals.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.runicrituals.RunicRituals;

public class RunicRitualsDamageTypes {
    public static final ResourceKey<DamageType> DISINTEGRATION_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, "disintegration"));

    public static void registerDamageTypes() {
        RunicRituals.LOGGER.info("Registering Damage Types");

    }
}
