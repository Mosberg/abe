package dk.mosberg.machine.fluidbarrel;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FluidBarrelScreenHandler extends ScreenHandler {
    public static final ExtendedScreenHandlerType<FluidBarrelScreenHandler> TYPE =
            new ExtendedScreenHandlerType<>(
                    (syncId, playerInventory, buf) -> new FluidBarrelScreenHandler(syncId,
                            playerInventory, new FluidBarrelInventory()));

    public FluidBarrelScreenHandler(int syncId, PlayerInventory playerInventory,
            PacketByteBuf buf) {
        this(syncId, playerInventory, new FluidBarrelInventory()); // No inventory sync needed for
                                                                   // simple machines
    }

    private final Inventory inventory;

    public FluidBarrelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new FluidBarrelInventory());
    }

    public FluidBarrelScreenHandler(int syncId, PlayerInventory playerInventory,
            Inventory inventory) {
        super(TYPE, syncId);
        this.inventory = inventory;
        // Add slots for empty/filled bottles
        this.addSlot(new Slot(inventory, 0, 56, 35)); // Empty
        this.addSlot(new Slot(inventory, 1, 116, 35)); // Filled
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

    public int getFluidAmount() {
        return 0; // TODO: sync real fluid amount from block entity
    }

    public int getMaxFluid() {
        return 1000; // TODO: sync real max fluid from block entity
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
