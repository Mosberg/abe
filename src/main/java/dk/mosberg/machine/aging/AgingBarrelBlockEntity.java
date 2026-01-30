package dk.mosberg.machine.aging;

import dk.mosberg.machine.MachineConstants;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AgingBarrelBlockEntity extends BlockEntity {
    // Slot 0: input bottle, Slot 1: output bottle
    private final SimpleInventory inv = new SimpleInventory(2);

    public int progress = 0;

    public AgingBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ABEBlockEntities.AGING_BARREL, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state,
            AgingBarrelBlockEntity be) {
        if (world.isClient)
            return;

        ItemStack in = be.inv.getStack(0);
        ItemStack out = be.inv.getStack(1);

        boolean canAgeBeer = in.isOf(ABEItems.BOTTLE_BEER)
                && (out.isEmpty() || out.isOf(ABEItems.BOTTLE_BEER_AGED));
        boolean canAgeSpirit = in.isOf(ABEItems.BOTTLE_SPIRIT)
                && (out.isEmpty() || out.isOf(ABEItems.BOTTLE_SPIRIT_AGED));

        if (!(canAgeBeer || canAgeSpirit)) {
            be.progress = 0;
            return;
        }

        if (!out.isEmpty() && out.getCount() >= out.getMaxCount()) {
            be.progress = 0;
            return;
        }

        be.progress++;
        if (be.progress >= MachineConstants.AGE_TIME_TICKS) {
            if (canAgeBeer) {
                in.decrement(1);
                be.insertOut(new ItemStack(ABEItems.BOTTLE_BEER_AGED, 1));
            } else {
                in.decrement(1);
                be.insertOut(new ItemStack(ABEItems.BOTTLE_SPIRIT_AGED, 1));
            }
            be.progress = 0;
        }

        be.markDirty();
    }

    private void insertOut(ItemStack stack) {
        ItemStack out = inv.getStack(1);
        if (out.isEmpty()) {
            inv.setStack(1, stack);
        } else {
            out.increment(stack.getCount());
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("Progress");
    }
}
