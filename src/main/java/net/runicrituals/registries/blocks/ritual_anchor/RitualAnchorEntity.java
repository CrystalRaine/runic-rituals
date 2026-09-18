package net.runicrituals.registries.blocks.ritual_anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.runicrituals.logic.ManaStorageHandler;
import net.runicrituals.logic.runes.RuneSequence;
import net.runicrituals.logic.ManaStorage;
import net.runicrituals.logic.runes.enums.RuneType;
import net.runicrituals.registries.RunicRitualsBlockEntities;
import net.runicrituals.registries.blocks.rune_slate.Runeslate;
import net.runicrituals.registries.blocks.rune_slate.RuneslateEntity;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.runicrituals.logic.Util.delta;

public class RitualAnchorEntity extends BlockEntity implements ManaStorage {
    private static final int RITUAL_SIZE_MAX = 50;

    UUID chainUUID = null;
    boolean linked = false;
    int runeChainSize = 0;
    List<BlockPos> chainStarts = new ArrayList<>();
    ManaStorageHandler mana = new ManaStorageHandler();

    RuneSequence sequence = null;

    public RitualAnchorEntity(BlockPos worldPosition, BlockState blockState) {
        super(RunicRitualsBlockEntities.RITUAL_ANCHOR_BLOCK_ENTITY, worldPosition, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RitualAnchorEntity blockEntity) {

//        Try to build the sequence 1x per second.
        if(blockEntity.sequence == null && level.getGameTime() % 20 == 0 && blockEntity.linked) {
            blockEntity.sequence = blockEntity.assembleSequence();
        }

//        if the sequence is incomplete or unbuilt, set sequence to null to be rebuilt 1s later.
//        otherwise, run the sequence / show active particles.
        if(blockEntity.sequence != null && blockEntity.sequence.size() == blockEntity.runeChainSize) {
            if (level.isClientSide() && blockEntity.isLinked() && level.getGameTime() % 5 == 0) {
                blockEntity.createParticle();

                for (BlockPos chainStart : blockEntity.chainStarts) {
                    blockEntity.createParticle(chainStart);
                }
            }

            blockEntity.sequence.run(level, blockEntity.mana, pos);
        } else {
            blockEntity.sequence = null;
        }
    }

    @Override
    public void setRemoved() {
        assert level != null;
        if(level.isClientSide() || !(Objects.requireNonNull(level.getServer()).isCurrentlySaving())) {
            delink();
        }
        super.setRemoved();
    }

    public boolean isLinked() {
        return linked;
    }

    public void link() {

        this.chainUUID = UUID.randomUUID();
        this.sequence = assembleSequence();
        this.runeChainSize = sequence.size();
        this.linked = true;
        this.mana.applyManaValue(-10);
        setChanged();
    }

    private RuneSequence assembleSequence() {
        assert level != null;

        RuneSequence seq = new RuneSequence();
        buildChains();

        // simple DFS search through tree built in buildChains.
        // basically, we want to build casting blocks in DFS order, but need to grab them in BFS order.
        List<RuneslateEntity> runeslates = new ArrayList<>();
        List<BlockPos> runeslateStack = new ArrayList<>(chainStarts);
        while(!runeslateStack.isEmpty()) {
            RuneslateEntity rse = Runeslate.getBlockEntity(level, runeslateStack.removeLast());
            if(rse == null) continue;

            runeslates.add(rse);
            runeslateStack.addAll(rse.getArguments());
        }

        seq.createCastingBlocks(level, runeslates);
        return seq;
    }

    public void delink() {
        assert level != null;

        List<RuneslateEntity> delinkQueue = new ArrayList<>(chainStarts.stream().map(p -> Runeslate.getBlockEntity(level, p)).filter(Objects::nonNull).toList());

        while(!delinkQueue.isEmpty()) {
            RuneslateEntity cursor = delinkQueue.removeFirst();
            delinkQueue.addAll(cursor.getArguments().stream().map(p -> Runeslate.getBlockEntity(level, p)).filter(Objects::nonNull).toList());
            cursor.clearOutData();
        }

        this.runeChainSize = 0;
        this.linked = false;
        this.chainUUID = null;
        this.mana = new ManaStorageHandler();
        this.sequence = new RuneSequence();
        this.chainStarts = new ArrayList<>();
        setChanged();
    }

    /**
     * technically runs a modified BFS to build a doubly-linked tree out of
     * RuneslateEntity-s by linking their anchors, arguments, and previous.
     * (where anchor is head, arguments is a list of next elements)
     */
    private int buildChains() {

        int size = 0;
        List<BlockPos> posStack = new ArrayList<>();
        posStack.add(getBlockPos());

        while (!posStack.isEmpty()) {
            assert level != null;

            BlockPos pos = posStack.removeFirst();
            RuneslateEntity rsePrev = Runeslate.getBlockEntity(level, pos);
            BlockPos prevPos =  rsePrev == null ? this.getBlockPos() : rsePrev.getPrevious();
            BlockPos delta = delta(prevPos, pos);
            if(delta.getX() == 0 && delta.getZ() == 0) {
                delta = new BlockPos(1, 0, 0);
            }

            // Elegant little loop I found https://www.reddit.com/r/adventofcode/comments/1qhewn6/rotating_between_eight_directions/
            // that rotates around the 8 cardinal + intercardinal directions (counterclockwise)
            // note i want the rotation for consistent linking behavior, or I'd use nested x/y -1 -> 1 loops for clarity
            for (int i = 0, x = delta.getX(), z = delta.getZ(); i < 8; i++) {

                // also! love me a block-break : )
                // this is here to ensure breaking still runs the x/y updates, even if the rest of the block should be skipped
                inner: {
                    BlockPos checkPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    RuneslateEntity rse = Runeslate.getBlockEntity(level, checkPos);

                    if (rse == null
                            || rse.isLinked()
                            || !(
                                rsePrev == null ?
                                    RuneType.ANCHOR.isValidChild(rse.getSymbolType()) :
                                    rsePrev.getSymbolType().isValidChild(rse.getSymbolType())
                            )
                            || rsePrev == rse
                            || !(
                                rsePrev == null
                                || rsePrev.getArguments().size() < rsePrev.getSymbolType().getChildCountMax()
                            )
                    ) {
                        break inner;
                    }

                    if(rsePrev == null) {
                        chainStarts.add(checkPos);
                    } else {
                        rsePrev.addArgument(rse);
                    }
                    rse.setAnchor(this);
                    rse.setChainUUID(this.chainUUID);
                    rse.setLinked();
                    rse.setPrevious(pos);
                    rse.setChanged();
                    posStack.addLast(checkPos);
                    if(size > RITUAL_SIZE_MAX) {
                        delink();
                        return 0;
                    }
                    size++;
                }

                int tx = x;
                int tz = z;
                x = sign(tx - tz);
                z = sign(tx + tz);
            }
        }
        return size;
    }

    private static int sign(double i) {
        return Double.compare(i, 0);
    }

    public void createParticle() {
        assert level != null;
        BlockPos pos = this.getBlockPos();

        level.addParticle(
            ParticleTypes.COPPER_FIRE_FLAME,
            pos.getX() + 0.5f,
            pos.getY() + 0.5f,
            pos.getZ() + 0.5f,
            0,
            0.03f,
            0
        );
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


    /* Save and Load */
    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        output.putBoolean("isLinked", linked);
        output.putDouble("mana", mana.getMana());
        output.putInt("runeChainSize", runeChainSize);

        if(chainUUID != null){
            output.putString("UUID", chainUUID.toString());
        }

        long[] primitiveLongArray = chainStarts.stream()
                .map(BlockPos::asLong)
                .mapToLong(Long::longValue)
                .toArray();
        output.putLongArray("chainStarts", primitiveLongArray);

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.linked = input.getBooleanOr("isLinked", false);
        this.runeChainSize = input.getIntOr("runeChainSize", 0);
        this.mana = new ManaStorageHandler(input.getDoubleOr("mana", 0));
        input.getString("UUID").ifPresent(uuid -> this.chainUUID = UUID.fromString(uuid));

        long[] primitiveArray = input.getOptionalLongArray("chainStarts").orElse(new long[]{});
        for(long i : primitiveArray) {
            chainStarts.add(BlockPos.of(i));
        }
    }

    
    /* Network management */
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

    public Object getRitualSize() {
        return sequence.size();
    }

    @Override
    public ManaStorageHandler getMana() {
        return mana;
    }
}
