package dk.mosberg.machine.condenser;

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

public class CondenserScreenHandler extends ScreenHandler {
    public static final ExtendedScreenHandlerType<CondenserScreenHandler> TYPE =
            new ExtendedScreenHandlerType<>((syncId, playerInventory, buf) -> {
                BlockPos pos = buf.readBlockPos();
                World world = playerInventory.player.getWorld();
                CondenserBlockEntity be = (CondenserBlockEntity) world.getBlockEntity(pos);
                return new CondenserScreenHandler(syncId, playerInventory, be);
            });

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    // Server-side constructor
    public CondenserScreenHandler(int syncId, PlayerInventory playerInventory,
            CondenserBlockEntity be) {
        super(TYPE, syncId);
        this.inventory = be.inv;
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0)
                    return be.progress;
                if (index == 1)
                    return MachineConstants.CONDENSE_TIME_TICKS;
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0)
                    be.progress = value;
            }

            @Override
            public int size() {
                return 2;
            }
        };
        this.addProperties(propertyDelegate);
        this.addSlot(new Slot(inventory, 0, 56, 53)); // Coolant
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
    public CondenserScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getClientBlockEntity(playerInventory, buf));
    }

    private static CondenserBlockEntity getClientBlockEntity(PlayerInventory playerInventory,
            PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        World world = playerInventory.player.getWorld();
        return (CondenserBlockEntity) world.getBlockEntity(pos);
    }

    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public int getMaxProgress() {
        return propertyDelegate.get(1);
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
