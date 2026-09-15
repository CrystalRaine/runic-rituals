package net.runicrituals.registries.blocks.rune_slate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneInlayMaterial;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.RuneType;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchor;
import net.runicrituals.registries.blocks.ritual_anchor.RitualAnchorEntity;
import net.runicrituals.registries.components.RuneDataComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class RuneslateEntity extends BlockEntity {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    UUID chainUUID = null;
    boolean linked = false;
    List<BlockPos> arguments = new ArrayList<>();
    BlockPos anchorPos = null;
    BlockPos prev = null;

    public RuneslateEntity(BlockPos worldPosition, BlockState blockState) {
        super(RunicRitualsBlockEntities.RUNESLATE_BLOCK_ENTITY, worldPosition, blockState);
    }

    public RitualAnchorEntity getAnchor() {
        assert level != null;
        if(anchorPos == null) return null;
        return RitualAnchor.getBlockEntity(level, anchorPos);
    }

    public void addArgument(RuneslateEntity rse) {
        arguments.add(rse.getBlockPos());
    }

    public void setAnchor(RitualAnchorEntity rse) {
        if(rse == null) {
            anchorPos = null;
            return;
        }
        anchorPos = rse.getBlockPos();
    }

    public Direction getFacing() {
        return getBlockState().getValue(FACING).getOpposite();
    }

    @Override
    public void setRemoved() {
        assert level != null;
        if(level.isClientSide() || !(Objects.requireNonNull(level.getServer()).isCurrentlySaving())) {
            if(getAnchor() != null) {
                getAnchor().delink();

                List<RuneslateEntity> delinkQueue = new ArrayList<>(arguments.stream().map(p -> Runeslate.getBlockEntity(level, p)).filter(Objects::nonNull).toList());
                while(!delinkQueue.isEmpty()) {
                    RuneslateEntity cursor = delinkQueue.removeFirst();
                    delinkQueue.addAll(cursor.getArguments().stream().map(p -> Runeslate.getBlockEntity(level, p)).filter(Objects::nonNull).toList());
                    cursor.clearOutData();
                }
            }
        }
        super.setRemoved();
    }

    public RuneDataComponent getRuneDataComponent() {
        return this.components().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE);
    }

    public RuneInlayMaterial getSlateInlay() {
        RuneDataComponent rdc = getRuneDataComponent();
        if(rdc != null) {
            return RuneInlayMaterial.getElementFromId(rdc.inlay());
        }
        return null;
    }

    public RuneSymbol getSlateSymbol() {
        RuneDataComponent rdc = getRuneDataComponent();
        if(rdc != null) {
            return RuneSymbol.getSymbolFromId(rdc.runeSymbol());
        }
        return null;
    }

    public RuneType getSymbolType() {
        return getSlateSymbol().getRuneType();
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        output.putBoolean("isLinked", linked);

        long[] primitiveLongArray = arguments.stream()
                .map(BlockPos::asLong)
                .mapToLong(Long::longValue)
                .toArray();

        output.putLongArray("argumentArray", primitiveLongArray);

        if(chainUUID != null){
            output.putString("UUID", chainUUID.toString());
        }
        if(anchorPos != null) {
            output.putLong("anchor", anchorPos.asLong());
        }
        if(prev != null) {
            output.putLong("previous", prev.asLong());
        }
        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.linked = input.getBooleanOr("isLinked", false);
        Long anchor = input.getLong("anchor").orElse(null);
        Long previous = input.getLong("previous").orElse(null);
        String uuid = input.getString("UUID").orElse(null);

        long[] primitiveArray = input.getOptionalLongArray("argumentArray").orElse(new long[]{});
        for(long i : primitiveArray) {
            arguments.add(BlockPos.of(i));
        }

        if(anchor != null) {
            this.anchorPos = BlockPos.of(anchor);
        }
        if(previous != null) {
            this.prev = BlockPos.of(previous);
        }
        if(uuid != null) {
            this.chainUUID = UUID.fromString(uuid);
        }
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    public void setChainUUID(UUID chainUUID) {
        this.chainUUID = chainUUID;
    }

    public UUID getChainUUID() {
        return chainUUID;
    }

    public BlockPos getPrevious() {
        return prev;
    }

    public void setPrevious(BlockPos pos) {
        prev = pos;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked() {
        linked = true;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RuneslateEntity runeslateEntity) {

        if(level.getGameTime() % 5 == 0 && runeslateEntity.isLinked()) {
            for (BlockPos bp : runeslateEntity.arguments) {
                runeslateEntity.createParticle(bp);
            }
        }
    }

    public void createParticle(BlockPos toward) {
        double speed = 15f;
        assert level != null;

        level.addParticle(
            ParticleTypes.COPPER_FIRE_FLAME,
            getBlockPos().getX() + 0.5f,
            getBlockPos().getY() + 0.1f,
            getBlockPos().getZ() + 0.5f,
            -(getBlockPos().getX() - toward.getX()) / speed,
            -(getBlockPos().getY() - toward.getY()) / speed,
            -(getBlockPos().getZ() - toward.getZ()) / speed
        );
    }

    public void delink() {
        if(anchorPos == null) return;
        getAnchor().delink();
    }

    public List<BlockPos> getArguments() {
        return arguments;
    }

    public void clearOutData() {
        chainUUID = null;
        linked = false;
        arguments = new ArrayList<>();
        anchorPos = null;
        prev = null;

        setChanged();
    }
}
