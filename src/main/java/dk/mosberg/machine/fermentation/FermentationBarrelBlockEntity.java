package dk.mosberg.machine.fermentation;

import dk.mosberg.machine.MachineConstants;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.registry.ABEItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FermentationBarrelBlockEntity extends BlockEntity
        implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.abe.fermentation_barrel");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory,
            PlayerEntity player) {
        return new FermentationBarrelScreenHandler(syncId, playerInventory, inv);
    }

    public void writeScreenOpeningData(PlayerEntity player, PacketByteBuf buf) {
        // Write any needed data for client sync here (none for now)
    }

    // Helper for NBT read/write for SingleVariantStorage
    public static void readTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        tank.variant = FluidVariant.fromNbt(nbt.getCompound("variant"));
        tank.amount = nbt.getLong("amount");
    }

    public static void writeTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        nbt.put("variant", tank.variant.toNbt());
        nbt.putLong("amount", tank.amount);
    }

    // Slot 0: yeast
    private final SimpleInventory inv = new SimpleInventory(1);

    public int progress = 0;

    // Input wort tank (south only)
    public final SingleVariantStorage<FluidVariant> wortTank = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return MachineConstants.MACHINE_TANK_4B;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return !variant.isBlank() && variant.getFluid() == ABEFluids.WORT_STILL;
        }

        @Override
        protected boolean canExtract(FluidVariant variant) {
            return true;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    // Output fermented tank (north only)
    public final SingleVariantStorage<FluidVariant> fermentedTank = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return MachineConstants.MACHINE_TANK_4B;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return false;
        }

        @Override
        protected boolean canExtract(FluidVariant variant) {
            return true;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    public FermentationBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.FERMENTATION_BARREL, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            FermentationBarrelBlockEntity be) {
        if (world.isClient)
            return;

        boolean hasYeast = be.inv.getStack(0).isOf(ABEItems.YEAST);
        boolean hasWort = be.wortTank.getAmount() >= FluidConstants.BUCKET;

        FluidVariant fermented = FluidVariant.of(ABEFluids.FERMENTED_STILL);
        long freeOut = MachineConstants.MACHINE_TANK_4B - be.fermentedTank.getAmount();

        if (!(hasYeast && hasWort && freeOut >= FluidConstants.BUCKET)) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.FERMENT_TIME_TICKS) {
            try (Transaction tx = Transaction.openOuter()) {
                long drained = be.wortTank.extract(FluidVariant.of(ABEFluids.WORT_STILL),
                        FluidConstants.BUCKET, tx);
                if (drained != FluidConstants.BUCKET)
                    return;

                be.inv.getStack(0).decrement(1);

                long inserted = be.fermentedTank.insert(fermented, FluidConstants.BUCKET, tx);
                if (inserted == FluidConstants.BUCKET)
                    tx.commit();
            }
            be.progress = 0;
        }

        be.markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
        NbtCompound in = new NbtCompound();
        writeTankNbt(wortTank, in);
        nbt.put("WortTank", in);
        NbtCompound out = new NbtCompound();
        writeTankNbt(fermentedTank, out);
        nbt.put("FermentedTank", out);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
        readTankNbt(wortTank, nbt.getCompound("WortTank"));
        readTankNbt(fermentedTank, nbt.getCompound("FermentedTank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side == Direction.SOUTH)
            return wortTank;
        if (side == Direction.NORTH)
            return fermentedTank;
        return null;
    }
}
