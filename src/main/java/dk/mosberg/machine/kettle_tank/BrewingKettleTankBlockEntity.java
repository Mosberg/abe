package dk.mosberg.machine.kettle_tank;

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

public class BrewingKettleTankBlockEntity extends BlockEntity {
    // Helper for NBT read/write for SingleVariantStorage
    public static void readTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        tank.variant = FluidVariant.fromNbt(nbt.getCompound("variant"));
        tank.amount = nbt.getLong("amount");
    }

    public static void writeTankNbt(SingleVariantStorage<FluidVariant> tank, NbtCompound nbt) {
        nbt.put("variant", tank.variant.toNbt());
        nbt.putLong("amount", tank.amount);
    }

    // Slots: 0 fuel, 1..10 ingredients (simple placeholder)
    private final SimpleInventory inv = new SimpleInventory(11);

    public int progress = 0;
    public int burnTime = 0;

    // Input water tank (bottom only)
    public final SingleVariantStorage<FluidVariant> waterTank = new SingleVariantStorage<>() {
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
            return !variant.isBlank() && variant.getFluid() == net.minecraft.fluid.Fluids.WATER;
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

    // Output wort tank (north only)
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

    public BrewingKettleTankBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.BREWING_KETTLE_TANK, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            BrewingKettleTankBlockEntity be) {
        if (world.isClient)
            return;

        boolean heated = HeatUtil.hasHeatBelow(world, pos);
        if (be.burnTime <= 0 && !heated)
            be.tryConsumeFuel();
        boolean hasPower = heated || be.burnTime > 0;
        if (be.burnTime > 0)
            be.burnTime--;

        boolean hasIngredients = be.hasAnyIngredient();
        boolean hasWater = be.waterTank.getAmount() >= FluidConstants.BUCKET;

        FluidVariant wort = FluidVariant.of(ABEFluids.WORT_STILL);
        long freeOut = MachineConstants.MACHINE_TANK_4B - be.wortTank.getAmount();

        if (!(hasPower && hasIngredients && hasWater && freeOut >= FluidConstants.BUCKET)) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.KETTLE_TIME_TICKS) {
            try (Transaction tx = Transaction.openOuter()) {
                long drained =
                        be.waterTank.extract(FluidVariant.of(net.minecraft.fluid.Fluids.WATER),
                                FluidConstants.BUCKET, tx);
                if (drained != FluidConstants.BUCKET)
                    return;

                be.consumeOneIngredient(tx);

                long inserted = be.wortTank.insert(wort, FluidConstants.BUCKET, tx);
                if (inserted == FluidConstants.BUCKET)
                    tx.commit();
            }
            be.progress = 0;
        }

        be.markDirty();
    }

    private boolean hasAnyIngredient() {
        for (int i = 1; i < inv.size(); i++) {
            if (!inv.getStack(i).isEmpty())
                return true;
        }
        return false;
    }

    private void consumeOneIngredient(Transaction tx) {
        for (int i = 1; i < inv.size(); i++) {
            ItemStack s = inv.getStack(i);
            if (!s.isEmpty()) {
                s.decrement(1);
                return;
            }
        }
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
        this.burnTime = bt;
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
        nbt.putInt("BurnTime", burnTime);
        NbtCompound in = new NbtCompound();
        writeTankNbt(waterTank, in);
        nbt.put("WaterTank", in);
        NbtCompound out = new NbtCompound();
        writeTankNbt(wortTank, out);
        nbt.put("WortTank", out);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
        burnTime = nbt.getInt("BurnTime");
        readTankNbt(waterTank, nbt.getCompound("WaterTank"));
        readTankNbt(wortTank, nbt.getCompound("WortTank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        // Bottom: accept water; North: output wort
        if (side == Direction.DOWN)
            return waterTank;
        if (side == Direction.NORTH)
            return wortTank;
        return null;
    }
}
