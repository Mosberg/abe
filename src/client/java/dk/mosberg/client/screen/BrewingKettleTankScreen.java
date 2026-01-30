
package dk.mosberg.client.screen;

import dk.mosberg.machine.kettle_tank.BrewingKettleTankScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BrewingKettleTankScreen extends HandledScreen<BrewingKettleTankScreenHandler> {
    // No texture needed; draw programmatically

    public BrewingKettleTankScreen(BrewingKettleTankScreenHandler handler,
            PlayerInventory inventory, Text title) {
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

        // Draw slot backgrounds
        for (int i = 0; i < 11; i++) {
            int sx = x + 8 + ((i - 1 + 10) % 5) * 18;
            int sy = y + 17 + ((i - 1 + 10) / 5) * 18;
            context.fill(sx, sy, sx + 18, sy + 18, 0xFF404040);
        }

        // Draw progress bar (horizontal)
        int progress = handler.getProgress();
        int maxProgress = 1; // TODO: sync real max progress if needed
        int progressWidth = (int) (24 * (progress / (float) Math.max(1, maxProgress)));
        if (progressWidth > 0) {
            context.fill(x + 79, y + 34, x + 79 + progressWidth, y + 34 + 16, 0xFF80FF80); // green
                                                                                           // bar
        }
    }
}
