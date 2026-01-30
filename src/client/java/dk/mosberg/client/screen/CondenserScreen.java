package dk.mosberg.client.screen;


import dk.mosberg.machine.condenser.CondenserScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;


public class CondenserScreen extends HandledScreen<CondenserScreenHandler> {
    // No texture needed; draw programmatically

    public CondenserScreen(CondenserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Draw main background rectangle
        context.fill(x, y, x + this.backgroundWidth, y + this.backgroundHeight, 0xFF202020); // dark
                                                                                             // gray

        // Draw slot background
        context.fill(x + 56, y + 53, x + 56 + 18, y + 53 + 18, 0xFF404040); // coolant slot

        // Draw progress bar (vertical)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressHeight = (int) (24 * (progress / (float) Math.max(1, maxProgress)));
        if (progressHeight > 0) {
            context.fill(x + 56, y + 15 + (24 - progressHeight), x + 56 + 16, y + 15 + 24,
                    0xFF80FF80); // green bar
        }
    }
}
