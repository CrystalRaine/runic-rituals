package net.runicrituals.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.entities.RitualBolt;

public class RunicRitualsEntities {
    public static ResourceKey<EntityType<?>> RITUAL_BOLT_KEY = create("ritual_bolt");
    
    private static ResourceKey<EntityType<?>> create(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, RunicRituals.id(name));
    }
    
    public static final EntityType<RitualBolt> RITUAL_BOLT = register(
        RITUAL_BOLT_KEY,
        EntityType.Builder.of(RitualBolt::new, MobCategory.MISC).noLootTable().sized(0.5F, 0.5F).eyeHeight(0.13F).clientTrackingRange(4).updateInterval(20)
    );
    
    private static <T extends Entity> EntityType<T> register(final ResourceKey<EntityType<?>> id, final EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id));
    }
    
    public static void registerEntities() {
    }
}
