package dk.mosberg.machine.fluidbarrel;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FluidBarrelBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.abe.fluid_barrel");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory,
            PlayerEntity player) {
        return new FluidBarrelScreenHandler(syncId, playerInventory, inv);
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

    // Slot 0: empty bottle, Slot 1: filled bottle
    private final SimpleInventory inv = new SimpleInventory(2);

    public final SingleVariantStorage<FluidVariant> tank = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return MachineConstants.MACHINE_TANK_16B;
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return !variant.isBlank();
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

    public FluidBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.FLUID_BARREL, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            FluidBarrelBlockEntity be) {
        if (world.isClient)
            return;

        ItemStack empty = be.inv.getStack(0);
        ItemStack out = be.inv.getStack(1);

        if (!empty.isOf(ABEItems.EMPTY_BOTTLE))
            return;
        if (be.tank.getAmount() < FluidConstants.BUCKET / 4)
            return; // treat bottle as 250mB

        ItemStack produced = be.mapFluidToBottle(be.tank.variant);
        if (produced.isEmpty())
            return;

        if (!out.isEmpty()
                && (!ItemStack.canCombine(out, produced) || out.getCount() >= out.getMaxCount()))
            return;

        try (Transaction tx = Transaction.openOuter()) {
            long drained = be.tank.extract(be.tank.variant, FluidConstants.BUCKET / 4, tx);
            if (drained != FluidConstants.BUCKET / 4)
                return;

            empty.decrement(1);
            if (out.isEmpty())
                be.inv.setStack(1, produced.copy());
            else
                out.increment(1);

            tx.commit();
        }

        be.markDirty();
    }

    private ItemStack mapFluidToBottle(FluidVariant variant) {
        if (variant.isBlank())
            return ItemStack.EMPTY;
        if (variant.getFluid() == ABEFluids.FERMENTED_STILL)
            return new ItemStack(ABEItems.BOTTLE_BEER, 1);
        if (variant.getFluid() == ABEFluids.CONDENSED_STILL)
            return new ItemStack(ABEItems.BOTTLE_SPIRIT, 1);
        return ItemStack.EMPTY;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound tankNbt = new NbtCompound();
        writeTankNbt(tank, tankNbt);
        nbt.put("Tank", tankNbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        readTankNbt(tank, nbt.getCompound("Tank"));
    }

    public Storage<FluidVariant> getFluidStorage(Direction side) {
        // Allow pipes from all sides; item IO handled by inventory.
        return tank;
    }
}
