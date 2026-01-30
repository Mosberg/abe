package dk.mosberg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.distillery.DistilleryScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DistilleryScreen extends HandledScreen<DistilleryScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("abe", "textures/gui/distillery.png");

    public DistilleryScreen(DistilleryScreenHandler handler, PlayerInventory inventory,
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

        // Progress bar (horizontal)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressWidth = (int) (24 * (progress / (float) maxProgress));
        if (progressWidth > 0) {
            drawTexture(matrices, x + 79, y + 34, 176, 14, progressWidth, 16);
        }
    }
}
