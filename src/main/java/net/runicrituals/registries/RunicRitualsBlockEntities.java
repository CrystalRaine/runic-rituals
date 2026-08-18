package net.runicrituals.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypeIds;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.RunicRituals;
import net.runicrituals.registries.blocks.rune_obelisk.RuneObeliskEntity;

public class RunicRitualsBlockEntities {

    public static final BlockEntityType<RuneObeliskEntity> RUNE_OBELISK_ENTITY_BLOCK_ENTITY = register("rune_obelisk_type", RuneObeliskEntity::new, RunicRitualsBlocks.RUNE_OBELISK);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(RunicRituals.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void registerBlockEntityTypes() {
        RunicRituals.LOGGER.info("Registering block entity types");
    }
}
