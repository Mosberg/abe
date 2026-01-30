package dk.mosberg.machine.distillery;

import dk.mosberg.machine.MachineConstants;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DistilleryScreenHandler extends ScreenHandler {
    public static final ExtendedScreenHandlerType<DistilleryScreenHandler> TYPE =
            new ExtendedScreenHandlerType<>((syncId, playerInventory, buf) -> {
                BlockPos pos = buf.readBlockPos();
                World world = playerInventory.player.getWorld();
                DistilleryBlockEntity be = (DistilleryBlockEntity) world.getBlockEntity(pos);
                return new DistilleryScreenHandler(syncId, playerInventory, be);
            });

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    // Server-side constructor
    public DistilleryScreenHandler(int syncId, PlayerInventory playerInventory,
            DistilleryBlockEntity be) {
        super(TYPE, syncId);
        this.inventory = be.inv;
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0)
                    return be.progress;
                if (index == 1)
                    return MachineConstants.DISTILL_TIME_TICKS;
                if (index == 2)
                    return be.burnTime;
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0)
                    be.progress = value;
                if (index == 2)
                    be.burnTime = value;
            }

            @Override
            public int size() {
                return 3;
            }
        };
        this.addProperties(propertyDelegate);
        this.addSlot(new Slot(inventory, 0, 56, 53)); // Fuel
        // Player inventory slots
        int m;
        for (m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    // Client-side constructor
    public DistilleryScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getClientBlockEntity(playerInventory, buf));
    }

    private static DistilleryBlockEntity getClientBlockEntity(PlayerInventory playerInventory,
            PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        World world = playerInventory.player.getWorld();
        return (DistilleryBlockEntity) world.getBlockEntity(pos);
    }

    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public int getMaxProgress() {
        return propertyDelegate.get(1);
    }

    public int getBurnTime() {
        return propertyDelegate.get(2);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // Implement shift-click logic if needed
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
