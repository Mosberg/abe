package dk.mosberg.client.screen;


import dk.mosberg.machine.distillery.DistilleryScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;


public class DistilleryScreen extends HandledScreen<DistilleryScreenHandler> {
    // No texture needed; draw programmatically

    public DistilleryScreen(DistilleryScreenHandler handler, PlayerInventory inventory,
            Text title) {
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
        context.fill(x + 56, y + 53, x + 56 + 18, y + 53 + 18, 0xFF404040); // fuel slot

        // Draw progress bar (horizontal)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressWidth = (int) (24 * (progress / (float) Math.max(1, maxProgress)));
        if (progressWidth > 0) {
            context.fill(x + 79, y + 34, x + 79 + progressWidth, y + 34 + 16, 0xFF80FF80); // green
                                                                                           // bar
        }
    }
}
