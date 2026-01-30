package dk.mosberg.machine.aging;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AgingBarrelScreenHandler extends ScreenHandler {
    public static final ExtendedScreenHandlerType<AgingBarrelScreenHandler> TYPE =
            new ExtendedScreenHandlerType<>(
                    (syncId, playerInventory, buf) -> new AgingBarrelScreenHandler(syncId,
                            playerInventory, new AgingBarrelInventory()));

    public AgingBarrelScreenHandler(int syncId, PlayerInventory playerInventory,
            PacketByteBuf buf) {
        this(syncId, playerInventory, new AgingBarrelInventory()); // No inventory sync needed for
                                                                   // simple machines
    }

    private final Inventory inventory;

    public AgingBarrelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new AgingBarrelInventory());
    }

    public AgingBarrelScreenHandler(int syncId, PlayerInventory playerInventory,
            Inventory inventory) {
        super(TYPE, syncId);
        this.inventory = inventory;
        // Add slots for input/output bottles
        this.addSlot(new Slot(inventory, 0, 56, 35)); // Input
        this.addSlot(new Slot(inventory, 1, 116, 35)); // Output
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

    public int getProgress() {
        return 0; // TODO: sync real progress from block entity
    }

    public int getMaxProgress() {
        return 100; // TODO: sync real max progress from block entity
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
