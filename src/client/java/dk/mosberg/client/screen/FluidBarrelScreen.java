package dk.mosberg.client.screen;


import dk.mosberg.machine.fluidbarrel.FluidBarrelScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;


public class FluidBarrelScreen extends HandledScreen<FluidBarrelScreenHandler> {
    // No texture needed; draw programmatically

    public FluidBarrelScreen(FluidBarrelScreenHandler handler, PlayerInventory inventory,
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

        // Draw slot backgrounds
        context.fill(x + 56, y + 35, x + 56 + 18, y + 35 + 18, 0xFF404040); // empty slot
        context.fill(x + 116, y + 35, x + 116 + 18, y + 35 + 18, 0xFF404040); // filled slot

        // Draw fluid tank level as a vertical bar
        int fluid = handler.getFluidAmount();
        int maxFluid = handler.getMaxFluid();
        int fluidHeight = (int) (24 * (fluid / (float) Math.max(1, maxFluid)));
        if (fluidHeight > 0) {
            context.fill(x + 56, y + 15 + (24 - fluidHeight), x + 56 + 16, y + 15 + 24, 0xFF8080FF); // blue
                                                                                                     // bar
        }
    }
}
