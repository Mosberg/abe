package dk.mosberg.machine.condenser;

import dk.mosberg.machine.MachineConstants;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEFluids;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CondenserBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.abe.condenser");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory,
            PlayerEntity player) {
        return new CondenserScreenHandler(syncId, playerInventory, this);
    }

    public void writeScreenOpeningData(PlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(getPos());
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

    // Slot 0: coolant (ice)
    public final SimpleInventory inv = new SimpleInventory(1);

    public int progress = 0;

    // Input distillate (south)
    public final SingleVariantStorage<FluidVariant> inTank = new SingleVariantStorage<>() {
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
            return !variant.isBlank() && variant.getFluid() == ABEFluids.DISTILLATE_STILL;
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

    // Output condensed (north)
    public final SingleVariantStorage<FluidVariant> outTank = new SingleVariantStorage<>() {
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

    public CondenserBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.CONDENSER, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            CondenserBlockEntity be) {
        if (world.isClient)
            return;

        boolean hasCoolant = be.isCoolant(be.inv.getStack(0));
        boolean hasIn = be.inTank.getAmount() >= FluidConstants.BUCKET;
        long freeOut = MachineConstants.MACHINE_TANK_4B - be.outTank.getAmount();

        if (!(hasCoolant && hasIn && freeOut >= FluidConstants.BUCKET)) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.CONDENSE_TIME_TICKS) {
            try (Transaction tx = Transaction.openOuter()) {
                long drained = be.inTank.extract(FluidVariant.of(ABEFluids.DISTILLATE_STILL),
                        FluidConstants.BUCKET, tx);
                if (drained != FluidConstants.BUCKET)
                    return;

                // consume coolant every batch (simple)
                be.inv.getStack(0).decrement(1);

                long inserted = be.outTank.insert(FluidVariant.of(ABEFluids.CONDENSED_STILL),
                        FluidConstants.BUCKET, tx);
                if (inserted == FluidConstants.BUCKET)
                    tx.commit();
            }
            be.progress = 0;
        }

        be.markDirty();
    }

    private boolean isCoolant(ItemStack stack) {
        return stack.isOf(Items.ICE) || stack.isOf(Items.PACKED_ICE) || stack.isOf(Items.SNOWBALL);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
        NbtCompound in = new NbtCompound();
        writeTankNbt(inTank, in);
        nbt.put("InTank", in);
        NbtCompound out = new NbtCompound();
        writeTankNbt(outTank, out);
        nbt.put("OutTank", out);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
        readTankNbt(inTank, nbt.getCompound("InTank"));
        readTankNbt(outTank, nbt.getCompound("OutTank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side == Direction.SOUTH)
            return inTank;
        if (side == Direction.NORTH)
            return outTank;
        return null;
    }
}
