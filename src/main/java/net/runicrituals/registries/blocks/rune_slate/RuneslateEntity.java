package net.runicrituals.registries.blocks.rune_slate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.RunicRituals;
import net.runicrituals.logic.RuneSequence;
import net.runicrituals.logic.RuneSymbol;
import net.runicrituals.logic.runes.ManaStorage;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.components.RuneDataComponent;
import net.runicrituals.registries.server_only.RunicRitualsComponents;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RuneslateEntity extends BlockEntity {

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final int RITUAL_SIZE_MAX = 50 * 3 * 4;

    int chainIndex = 0;
    UUID chainUUID = null;
    ManaStorage mana = new ManaStorage();

    boolean isAnchor = false;
    boolean isLinked = false;

    BlockPos nextPos = null;
    BlockPos anchorPos = null;

    List<RuneDataComponent> runeComponents = null;

    List<BlockPos> base = null;
    BlockPos baseMax = null;
    BlockPos baseMin = null;

    public RuneslateEntity(BlockPos worldPosition, BlockState blockState) {
        super(RunicRitualsBlockEntities.RUNESLATE_BLOCK_ENTITY, worldPosition, blockState);
    }

    public RuneslateEntity getAnchor() {
        if(anchorPos == null) return null;
        assert level != null;
        BlockEntity anchor = level.getBlockEntity(anchorPos);
        if(!(anchor instanceof RuneslateEntity)) return null;
        return (RuneslateEntity) level.getBlockEntity(anchorPos);
    }

    public void setAnchor(RuneslateEntity rse) {
        if(rse == null) {
            anchorPos = null;
            return;
        }
        anchorPos = rse.getBlockPos();
    }

    public RuneslateEntity getNext() {
        if(nextPos == null) return null;
        assert level != null;
        BlockEntity next = level.getBlockEntity(nextPos);
        if(!(next instanceof RuneslateEntity)) return null;
        return (RuneslateEntity) level.getBlockEntity(nextPos);
    }

    public Direction getFacing() {
        return getBlockState().getValue(FACING).getOpposite();
    }

    public void setNext(RuneslateEntity rse) {
        if(rse == null) {
            nextPos = null;
            return;
        }
        nextPos = rse.getBlockPos();
    }

    public void setRemoved() {
        assert level != null;
        if(!(!level.isClientSide() && Objects.requireNonNull(level.getServer()).isCurrentlySaving())) {
            if(getAnchor() != null) {
                getAnchor().delink();
            }
            this.delink();
        }
        super.setRemoved();
    }


    // this is... not particularly performant lol
    // only want to run this once when the loop is linked initially, then save result off.
    public boolean getBase() {

        baseMax = new BlockPos.MutableBlockPos(Integer.MIN_VALUE, anchorPos.getY(), Integer.MIN_VALUE);
        baseMin = new BlockPos.MutableBlockPos(Integer.MAX_VALUE, anchorPos.getY(), Integer.MAX_VALUE);

        RuneslateEntity rse = getAnchor();
        do {
            BlockPos rsePos = rse.getBlockPos();
            if(rsePos.getX() > baseMax.getX()) {
                baseMax = baseMax.mutable().setX(rsePos.getX());
            }
            if(rsePos.getZ() > baseMax.getZ()) {
                baseMax = baseMax.mutable().setZ(rsePos.getZ());
            }
            if(rsePos.getX() < baseMin.getX()) {
                baseMin = baseMin.mutable().setX(rsePos.getX());
            }
            if(rsePos.getZ() < baseMin.getZ()) {
                baseMin = baseMin.mutable().setZ(rsePos.getZ());
            }

            rse = rse.getNext();
        } while (!rse.isAnchor);

        Direction direction = Direction.NORTH;
        boolean inside = false;
        List<BlockPos> fill = null;
        int edgesTouched = 0;

        for(int i = 0; i < 4; i++) {
            BlockPos check = anchorPos.relative(direction);

            BlockListAndEdges fillTmp = floodFill(check, baseMax, baseMin);
            if(fillTmp != null && !fillTmp.blocks.isEmpty() && fillTmp.edgesTouched > edgesTouched) {
                fill = fillTmp.blocks;
                edgesTouched = fillTmp.edgesTouched;
                inside = true;
            }
            direction = direction.getClockWise();
        }

        if(inside) {
            fill.forEach(this::createParticle);
            this.base = fill.stream().sorted().toList();
        }

        return inside;
    }

    public boolean isLinked() {
        return isLinked;
    }

    private record BlockListAndEdges(List<BlockPos> blocks, int edgesTouched) {}

    private BlockListAndEdges floodFill(BlockPos pos, BlockPos max, BlockPos min) {
        List<BlockPos> positions = new ArrayList<>();
        List<BlockPos> queue = new ArrayListDeque<>();
        queue.add(pos);
        int edgesTouched = 0;

        while(!queue.isEmpty()) {
            BlockPos grabbed = queue.removeFirst();

            assert level != null;
            BlockEntity be = level.getBlockEntity(grabbed);

            // fill edge (TBD make sure is part of the ritual)
            // this is necessary to be added, since we want rituals in a "B" shape to contain all encapsulated blocks
            if((be instanceof RuneslateEntity) && ((RuneslateEntity) be).chainUUID == chainUUID) {
                edgesTouched++;
                continue;
            }

            if(positions.contains(grabbed)) {
                continue;
            }

            // if you try to go out of bounds, fill would be infinite
            if(!isInBounds(grabbed, max, min)) {
                return null;
            }

            positions.add(grabbed);
            if(!queue.contains(grabbed.north())) queue.addLast(grabbed.north());
            if(!queue.contains(grabbed.east())) queue.addLast(grabbed.east());
            if(!queue.contains(grabbed.south())) queue.addLast(grabbed.south());
            if(!queue.contains(grabbed.west())) queue.addLast(grabbed.west());
        }
        return new BlockListAndEdges(positions, edgesTouched);
    }

    private boolean isInBounds(BlockPos pos, BlockPos max, BlockPos min) {
        int posX = pos.getX();
        int posZ = pos.getZ();
        return posX < max.getX() && posX > min.getX() && posZ < max.getZ() && posZ > min.getZ();
    }

    public void delink() {
        RuneslateEntity rse = this;
        rse.base = null;
        rse.runeComponents = null;
        rse.mana = new ManaStorage();
        rse.baseMax = null;
        rse.baseMin = null;
        rse.chainIndex = 0;
        rse.chainUUID = null;

        while(rse != null) {
            RuneslateEntity tmpNext = rse.getNext();
            rse.setNext(null);
            rse.setAnchor(null);

            rse.isLinked = false;
            rse.isAnchor = false;

            rse.chainIndex = 0;
            rse.setChanged();
            rse = tmpNext;
        }
    }

    public void link() {
        if(isLinked) return;

        assert level != null;
        RuneslateEntity currentRse = this;

        isAnchor = true;
        chainUUID = UUID.randomUUID();
        setAnchor(this);

        do {
            RuneslateEntity nextRse = findNextLink(currentRse);
            if(nextRse == null) break;
            currentRse.link(nextRse);
            currentRse = nextRse;

        } while (currentRse.getNext() == null && !currentRse.isAnchor);

        assembleSequence();
    }

    private void assembleSequence() {
        if(!isAnchor) return;
        assert level != null;
        RuneslateEntity currentRse = this;
        List<RuneDataComponent> componentList = new ArrayList<>();

        do {
            if(currentRse.components().has(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE)) {
                componentList.add(currentRse.components().get(RunicRitualsComponents.RUNE_DATA_COMPONENT_TYPE));
            }
            currentRse = currentRse.getNext();
        } while (currentRse != null && currentRse.getNext() != null && !currentRse.isAnchor);

        // only update list if whole circle is consumed
        if(currentRse == null || !currentRse.isAnchor) return;

        runeComponents = componentList;
    }

    private RuneslateEntity findNextLink(RuneslateEntity currentRse) {
        Direction startDir = currentRse.getBlockState().getValue(FACING).getOpposite();

        // Elegant little loop I found https://www.reddit.com/r/adventofcode/comments/1qhewn6/rotating_between_eight_directions/
        // that rotates around the 8 cardinal + intercardinal directions (counterclockwise)
        // note i want the rotation for consistent linking behavior, or I'd use nested x/y -1 -> 1 loops for clarity
        for(int i = 0, x = startDir.getStepX(), z = startDir.getStepZ(); i < 8; i++) {

            // also! love me a block-break : )
            // this is here to ensure breaking still runs the x/y updates, even if the rest of the block should be skipped
            inner: {
                assert level != null;
                if (currentRse.nextPos != null) break inner;
                if (currentRse.chainIndex > RITUAL_SIZE_MAX) break inner;

                BlockPos rsePos = currentRse.getBlockPos();
                RuneslateEntity tmpNext = Runeslate.getBlockEntity(level, new BlockPos(rsePos.getX() + x, rsePos.getY(), rsePos.getZ() + z));

                if (tmpNext == null) break inner;
                if (tmpNext.isLinked) break inner;

                return tmpNext;
            }

            int tx = x;
            int tz = z;
            x = sign(tx-tz);
            z = sign(tx+tz);
        }
        return null;
    }

    private void link(RuneslateEntity next) {
        this.setNext(next);
        next.anchorPos = anchorPos;
        next.chainUUID = chainUUID;
        next.isLinked = true;
        next.chainIndex = chainIndex + 1;
        setChanged();
        next.setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RuneslateEntity blockEntity) {

        if(level.getGameTime() % 5 == 0 && blockEntity.isLinked) {
            blockEntity.createParticle();
        }

        if(blockEntity.isAnchor) {
            if(blockEntity.runeComponents == null || blockEntity.runeComponents.isEmpty()) {
                blockEntity.assembleSequence();
            }

            if(blockEntity.runeComponents != null) {
                // run sequence
                RuneSequence.run(level, blockEntity.mana, blockEntity.runeComponents, blockEntity.base, blockEntity.baseMin, blockEntity.baseMax);
            }
        }
    }

    public void createParticle() {
        double speed = 15f;
        assert level != null;

        if(nextPos == null) {
            level.addParticle(
                    isAnchor ? ParticleTypes.COPPER_FIRE_FLAME : ParticleTypes.FLAME,
                    getBlockPos().getX() + 0.5f,
                    getBlockPos().getY() + 0.1f,
                    getBlockPos().getZ() + 0.5f,
                    0,
                    0.03f,
                    0
            );
            return;
        }
        level.addParticle(
                isAnchor ? ParticleTypes.COPPER_FIRE_FLAME : ParticleTypes.FLAME,
                getBlockPos().getX() + 0.5f,
                getBlockPos().getY() + 0.1f,
                getBlockPos().getZ() + 0.5f,
                -(getBlockPos().getX() - nextPos.getX()) / speed,
                -(getBlockPos().getY() - nextPos.getY()) / speed,
                -(getBlockPos().getZ() - nextPos.getZ()) / speed
        );
    }

    public void createParticle(BlockPos pos) {
        assert level != null;

        level.addParticle(
                isAnchor ? ParticleTypes.COPPER_FIRE_FLAME : ParticleTypes.FLAME,
                pos.getX() + 0.5f,
                pos.getY() + 0.5f,
                pos.getZ() + 0.5f,
                0,
                0.03f,
                0
        );
    }

    private static int sign(double i) {
        return Double.compare(i, 0);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        output.putBoolean("isAnchor", isAnchor);
        output.putBoolean("isLinked", isLinked);
        output.putInt("chainIndex", chainIndex);
        output.putDouble("mana", mana.getMana());

        if(nextPos != null){
            output.putLong("next", nextPos.asLong());
        }

        if(chainUUID != null){
            output.putString("UUID", chainUUID.toString());
        }

        if(anchorPos != null) {
            output.putLong("anchor", anchorPos.asLong());
        }

        if(baseMax != null) {
            output.putLong("baseMax", baseMax.asLong());
        }
        if(baseMin != null) {
            output.putLong("baseMin", baseMin.asLong());
        }

        if(base != null) {
            long[] longs = new long[base.size()];
            for(int i = 0; i < base.size(); i++) {
                longs[i] = base.get(i).asLong();
            }
            output.putLongArray("baseBlocks", longs);
        }

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.isAnchor = input.getBooleanOr("isAnchor", false);
        this.isLinked = input.getBooleanOr("isLinked", false);
        this.chainIndex = input.getIntOr("chainIndex", 0);

        double m = input.getDoubleOr("mana", 0);
        Long next = input.getLong("next").orElse(null);
        Long anchor = input.getLong("anchor").orElse(null);
        Long baseMax = input.getLong("baseMax").orElse(null);
        Long baseMin = input.getLong("baseMin").orElse(null);
        String uuid = input.getString("UUID").orElse(null);
        long[] longs = input.getOptionalLongArray("baseBlocks").orElse(null);

        this.mana = new ManaStorage(m);

        if(next != null) {
            this.nextPos = BlockPos.of(next);
        }
        if(anchor != null) {
            this.anchorPos = BlockPos.of(anchor);
        }
        if(uuid != null) {
            this.chainUUID = UUID.fromString(uuid);
        }
        if(baseMax != null) {
            this.baseMax = BlockPos.of(baseMax);
        }
        if(baseMin != null) {
            this.baseMin = BlockPos.of(baseMin);
        }
        if(longs != null) {
            this.base = new ArrayList<>();
            for(long i : longs) {
                base.add(BlockPos.of(i));
            }
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
}
