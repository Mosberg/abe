package dk.mosberg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.fluidbarrel.FluidBarrelScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FluidBarrelScreen extends HandledScreen<FluidBarrelScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier("abe", "textures/gui/fluid_barrel.png");

    public FluidBarrelScreen(FluidBarrelScreenHandler handler, PlayerInventory inventory,
            Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(net.minecraft.client.util.math.MatrixStack matrices, float delta,
            int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // Optional: Draw fluid tank level as a vertical bar
        int fluid = handler.getFluidAmount();
        int maxFluid = handler.getMaxFluid();
        int fluidHeight = (int) (24 * (fluid / (float) maxFluid));
        if (fluidHeight > 0) {
            drawTexture(matrices, x + 56, y + 15 + (24 - fluidHeight), 192, 31 + (24 - fluidHeight),
                    16, fluidHeight);
        }
    }
}
