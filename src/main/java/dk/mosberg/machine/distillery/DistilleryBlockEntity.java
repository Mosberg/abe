package dk.mosberg.machine.distillery;

import dk.mosberg.machine.MachineConstants;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.util.HeatUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DistilleryBlockEntity extends BlockEntity {
    // Helper for NBT read/write for SingleVariantStorage
    public static void readTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        tank.variant = FluidVariant.fromNbt(nbt.getCompound("variant"));
        tank.amount = nbt.getLong("amount");
    }

    public static void writeTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        nbt.put("variant", tank.variant.toNbt());
        nbt.putLong("amount", tank.amount);
    }

    // Slot 0: fuel
    private final SimpleInventory inv = new SimpleInventory(1);

    public int progress = 0;
    public int burnTime = 0;

    // Input fermented (any side, but spec says "inputs" (south works fine))
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
            return !variant.isBlank() && variant.getFluid() == ABEFluids.FERMENTED_STILL;
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

    // Output distillate (north)
    public final SingleVariantStorage<FluidVariant> distillateTank = new SingleVariantStorage<>() {
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

    public DistilleryBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.DISTILLERY, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            DistilleryBlockEntity be) {
        if (world.isClient)
            return;

        boolean heated = HeatUtil.hasHeatBelow(world, pos);
        if (be.burnTime <= 0 && !heated)
            be.tryConsumeFuel();
        boolean hasPower = heated || be.burnTime > 0;
        if (be.burnTime > 0)
            be.burnTime--;

        boolean hasIn = be.fermentedTank.getAmount() >= FluidConstants.BUCKET;
        long freeOut = MachineConstants.MACHINE_TANK_4B - be.distillateTank.getAmount();

        if (!(hasPower && hasIn && freeOut >= FluidConstants.BUCKET)) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.DISTILL_TIME_TICKS) {
            try (Transaction tx = Transaction.openOuter()) {
                long drained = be.fermentedTank.extract(FluidVariant.of(ABEFluids.FERMENTED_STILL),
                        FluidConstants.BUCKET, tx);
                if (drained != FluidConstants.BUCKET)
                    return;

                long inserted = be.distillateTank.insert(
                        FluidVariant.of(ABEFluids.DISTILLATE_STILL), FluidConstants.BUCKET, tx);
                if (inserted == FluidConstants.BUCKET)
                    tx.commit();
            }
            be.progress = 0;
        }

        be.markDirty();
    }

    private void tryConsumeFuel() {
        ItemStack fuel = inv.getStack(0);
        if (fuel.isEmpty())
            return;

        int bt = 0;
        if (fuel.isOf(Items.COAL) || fuel.isOf(Items.CHARCOAL))
            bt = 20 * 60;
        else if (fuel.isOf(Items.COAL_BLOCK))
            bt = 20 * 60 * 10;

        if (bt <= 0)
            return;
        fuel.decrement(1);
        burnTime = bt;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
        nbt.putInt("BurnTime", burnTime);
        NbtCompound in = new NbtCompound();
        writeTankNbt(fermentedTank, in);
        nbt.put("FermentedTank", in);
        NbtCompound out = new NbtCompound();
        writeTankNbt(distillateTank, out);
        nbt.put("DistillateTank", out);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
        burnTime = nbt.getInt("BurnTime");
        readTankNbt(fermentedTank, nbt.getCompound("FermentedTank"));
        readTankNbt(distillateTank, nbt.getCompound("DistillateTank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side == Direction.NORTH)
            return distillateTank;
        if (side == Direction.SOUTH)
            return fermentedTank;
        return null;
    }
}
