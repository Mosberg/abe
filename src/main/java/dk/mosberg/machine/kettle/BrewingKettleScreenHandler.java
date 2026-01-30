package dk.mosberg.machine.kettle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class BrewingKettleScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<BrewingKettleScreenHandler> TYPE =
            new ScreenHandlerType<>((syncId,
                    playerInventory) -> new BrewingKettleScreenHandler(syncId, playerInventory),
                    null);

    private final Inventory inventory;
    private int progress;
    private int maxProgress;

    public BrewingKettleScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public BrewingKettleScreenHandler(int syncId, PlayerInventory playerInventory,
            Inventory inventory) {
        super(TYPE, syncId);
        this.inventory = inventory;
        // Fuel slot
        this.addSlot(new Slot(inventory, 0, 56, 53));
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

    // Call these from your block entity sync logic
    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress == 0 ? 1 : maxProgress;
    }

    public void setProgress(int value) {
        this.progress = value;
    }

    public void setMaxProgress(int value) {
        this.maxProgress = value;
    }


    @Override
    public net.minecraft.item.ItemStack quickMove(PlayerEntity player, int index) {
        // Implement logic for shift-click item transfer, or return ItemStack.EMPTY for now
        return net.minecraft.item.ItemStack.EMPTY;
    }
}
