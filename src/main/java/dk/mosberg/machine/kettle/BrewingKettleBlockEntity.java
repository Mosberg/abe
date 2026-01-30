package dk.mosberg.machine.kettle;

import dk.mosberg.machine.MachineConstants;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.util.HeatUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BrewingKettleBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.abe.brewing_kettle");
    }

    @Override
    public ScreenHandler createMenu(int syncId,
            net.minecraft.entity.player.PlayerInventory playerInventory,
            net.minecraft.entity.player.PlayerEntity player) {
        return new BrewingKettleScreenHandler(syncId, playerInventory, inv);
    }

    // Slot 0: fuel
    private final SimpleInventory inv = new SimpleInventory(1);

    public int progress = 0;
    public int burnTime = 0;
    public int burnTimeTotal = 0;

    // Output tank (wort) - TOP only
    public final SingleFluidStorage wortTank =
            SingleFluidStorage.withFixedCapacity(MachineConstants.MACHINE_TANK_4B, this::markDirty);


    // Remove invalid overrides (not overriding superclass)
    protected boolean canInsert(FluidVariant variant) {
        return true;
    }

    protected boolean canExtract(FluidVariant variant) {
        return true;
    }


    public BrewingKettleBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.BREWING_KETTLE, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            BrewingKettleBlockEntity be) {
        if (world.isClient)
            return;

        boolean heated = HeatUtil.hasHeatBelow(world, pos);
        if (be.burnTime <= 0 && !heated)
            be.tryConsumeFuel();

        boolean hasPower = heated || be.burnTime > 0;
        if (be.burnTime > 0)
            be.burnTime--;

        FluidVariant wort = FluidVariant.of(ABEFluids.WORT_STILL);
        long free = MachineConstants.MACHINE_TANK_4B - be.wortTank.getAmount();
        boolean canMake = hasPower && free >= FluidConstants.BUCKET;

        if (!canMake) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.KETTLE_TIME_TICKS) {
            try (Transaction tx = Transaction.openOuter()) {
                long inserted = be.wortTank.insert(wort, FluidConstants.BUCKET, tx);
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

        int bt = fuelToBurnTime(fuel);
        if (bt <= 0)
            return;

        fuel.decrement(1);
        this.burnTime = bt;
        this.burnTimeTotal = bt;
        markDirty();
    }

    private static int fuelToBurnTime(ItemStack stack) {
        if (stack.isOf(Items.COAL))
            return 20 * 60;
        if (stack.isOf(Items.CHARCOAL))
            return 20 * 60;
        if (stack.isOf(Items.COAL_BLOCK))
            return 20 * 60 * 10;
        return 0;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
        nbt.putInt("BurnTime", burnTime);
        nbt.putInt("BurnTotal", burnTimeTotal);

        NbtCompound tank = new NbtCompound();
        wortTank.writeNbt(tank);
        nbt.put("WortTank", tank);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
        burnTime = nbt.getInt("BurnTime");
        burnTimeTotal = nbt.getInt("BurnTotal");
        wortTank.readNbt(nbt.getCompound("WortTank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        return side == Direction.UP ? wortTank : null;
    }
}
