
package dk.mosberg.machine.kettle_tank;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BrewingKettleTankScreenHandler extends ScreenHandler {
    public static final ExtendedScreenHandlerType<BrewingKettleTankScreenHandler> TYPE =
            new ExtendedScreenHandlerType<>(
                    (syncId, playerInventory, buf) -> new BrewingKettleTankScreenHandler(syncId,
                            playerInventory, new SimpleInventory(11)));

    public BrewingKettleTankScreenHandler(int syncId, PlayerInventory playerInventory,
            PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(11)); // TODO: read inventory from buf if
                                                                // needed
    }

    private final Inventory inventory;
    private int progress;
    private int burnTime;

    public BrewingKettleTankScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(11));
    }

    public BrewingKettleTankScreenHandler(int syncId, PlayerInventory playerInventory,
            Inventory inventory) {
        super(TYPE, syncId);
        this.inventory = inventory;
        // Fuel slot
        this.addSlot(new Slot(inventory, 0, 56, 53));
        // Ingredient slots (1..10)
        for (int i = 1; i <= 10; i++) {
            this.addSlot(new Slot(inventory, i, 8 + ((i - 1) % 5) * 18, 17 + ((i - 1) / 5) * 18));
        }
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public int getProgress() {
        return progress;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setProgress(int value) {
        this.progress = value;
    }

    public void setBurnTime(int value) {
        this.burnTime = value;
    }

    @Override
    public net.minecraft.item.ItemStack quickMove(PlayerEntity player, int index) {
        // Implement logic for shift-click item transfer, or return ItemStack.EMPTY for now
        return net.minecraft.item.ItemStack.EMPTY;
    }
}
