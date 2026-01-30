package dk.mosberg.client.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.fermentation.FermentationBarrelScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class FermentationBarrelScreen extends HandledScreen<FermentationBarrelScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier("abe", "textures/gui/fermentation_barrel.png");

    public FermentationBarrelScreen(FermentationBarrelScreenHandler handler,
            PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // Progress bar (vertical)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressHeight = (int) (24 * (progress / (float) maxProgress));
        if (progressHeight > 0) {
            context.drawTexture(TEXTURE, x + 56, y + 15 + (24 - progressHeight), 176,
                    31 + (24 - progressHeight), 16, progressHeight);
        }
    }
}
