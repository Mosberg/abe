package dk.mosberg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.kettle.BrewingKettleScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BrewingKettleScreen extends HandledScreen<BrewingKettleScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/gui/container/furnace.png");

    public BrewingKettleScreen(BrewingKettleScreenHandler handler, PlayerInventory inventory,
            Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256,
                256);

        // Progress bar (horizontal)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressWidth = (int) (24 * (progress / (float) maxProgress));
        if (progressWidth > 0) {
            context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, progressWidth, 16, 256, 256);
        }
    }
}
