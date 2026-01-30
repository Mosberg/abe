# GUIs without custom textures

Instead of using custom textures for your GUI screens in Minecraft mods, you can either draw the GUI elements programmatically using the `DrawContext` methods or utilize existing vanilla Minecraft GUI textures located in the `minecraft/textures/gui/` directory.

```java
package dk.mosberg.client.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.aging.AgingBarrelScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class AgingBarrelScreen extends HandledScreen<AgingBarrelScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier("abe", "textures/gui/aging_barrel.png");


    public AgingBarrelScreen(AgingBarrelScreenHandler handler, PlayerInventory inventory,
            Text title) {
        super(handler, inventory, title);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);


        // Progress bar (horizontal)
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressWidth = (int) (24 * (progress / (float) maxProgress));
        if (progressWidth > 0) {
            context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, progressWidth, 16);
        }
    }
}
```

instead of using textures/gui/_.png files for the screens use coding to draw them or use minecraft vanilla gui textures: minecraft/textures/gui/_.png, minecraft/textures/gui/**/\*.png and minecraft/textures/gui/**/\*_/_.png

[Java Edition GUI textures – Minecraft Wiki](https://minecraft.wiki/w/Java_Edition_GUI_textures)

### Example 1

<think>
The user wants to modify the Minecraft mod code to not use custom textures for the GUI screens. Instead, use coding to draw them or use vanilla Minecraft GUI textures from minecraft/textures/gui/*.png, etc.

## Investigating mod customization

- The user aims to alter a Minecraft mod to avoid custom GUI textures.
- They prefer drawing GUIs through code or using vanilla Minecraft GUI textures from minecraft/textures/gui/\*.png.
  </think>

```java
package dk.mosberg.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dk.mosberg.machine.aging.AgingBarrelScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AgingBarrelScreen extends HandledScreen<AgingBarrelScreenHandler> {
// Use vanilla furnace GUI texture instead of custom texture
private static final Identifier TEXTURE =
new Identifier("minecraft", "textures/gui/container/furnace.png");

    public AgingBarrelScreen(AgingBarrelScreenHandler handler, PlayerInventory inventory,
            Text title) {
        super(handler, inventory, title);
        // Set background size to match vanilla furnace GUI (176x166)
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // Progress bar (horizontal) - using the vanilla arrow from furnace texture
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();
        int progressWidth = (int) (24 * (progress / (float) maxProgress));
        if (progressWidth > 0) {
            context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, progressWidth, 16);
        }
    }

}

```

### Explanation of Changes:

- **Texture Replacement**: Changed the `TEXTURE` identifier to use the vanilla Minecraft furnace GUI texture (`minecraft:textures/gui/container/furnace.png`). This avoids custom textures while providing a similar look for a machine with progress (e.g., aging barrel).
- **Background Size**: Explicitly set `backgroundWidth` and `backgroundHeight` in the constructor to match the vanilla furnace GUI dimensions (176x166 pixels). This ensures proper rendering without relying on custom sizing.
- **Progress Bar**: Retained the existing progress bar drawing logic, as it uses the vanilla arrow sprite from the furnace texture (starting at texture coordinates 176,14). This fits the requirement to use vanilla GUI textures.
- **No Custom Textures**: The code no longer references any custom `abe:textures/gui/*.png` files. If you prefer to draw the GUI entirely programmatically (without any textures), you could replace the texture drawing with `DrawContext` methods like `fill()` for rectangles, but using vanilla textures is simpler and maintains a consistent Minecraft look. For example, to draw a basic gray background and green progress bar programmatically:

```java
  // In drawBackground, replace texture drawing with:
  // Draw gray background
  context.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xFF8B8B8B);
  // Draw green progress bar
  int progressWidth = (int) (24 * (progress / (float) maxProgress));
  if (progressWidth > 0) {
      context.fill(x + 79, y + 34, x + 79 + progressWidth, y + 34 + 16, 0xFF00FF00);
  }
```

However, the current implementation uses vanilla textures as requested. If the vanilla furnace layout doesn't perfectly match your aging barrel GUI (e.g., different slot positions), you may need to adjust coordinates or choose a different vanilla texture (e.g., from the wiki link you provided, like `minecraft:textures/gui/container/brewing_stand.png` for a different machine style). Let me know if you need further adjustments!

### Example 2

You can drop your mod-owned `textures/gui/*.png` by (1) switching `TEXTURE` to a **vanilla** GUI texture under the `minecraft` namespace, or (2) drawing the panel/progress bar directly with `DrawContext` drawing helpers. [maven.fabricmc](https://maven.fabricmc.net/docs/yarn-1.21+build.7/net/minecraft/client/gui/DrawContext.html)

## Option A: Use vanilla container textures

Pick a vanilla background (for example the generic 54-slot chest UI) and draw the same two “slices” vanilla uses for container + player inventory. [creativemechanicserver](https://www.creativemechanicserver.com/static/assets/minecraft/textures/gui/container/)

```java
private static final Identifier VANILLA_BG =
        new Identifier("minecraft", "textures/gui/container/generic_54.png");

@Override
protected void init() {
    super.init();
    // If your handler is not 6 rows, adjust these like the vanilla generic container does.
    this.backgroundWidth = 176;
    this.backgroundHeight = 114 + 6 * 18;
}

@Override
protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    int x = (this.width - this.backgroundWidth) / 2;
    int y = (this.height - this.backgroundHeight) / 2;

    // Top: container area (6 rows -> 6*18 + 17 px)
    context.drawTexture(VANILLA_BG, x, y, 0, 0, 0,
            this.backgroundWidth, 6 * 18 + 17,
            256, 256);

    // Bottom: player inventory area (96 px tall) starting at V=126 in generic_54.png
    context.drawTexture(VANILLA_BG, x, y + 6 * 18 + 17, 0, 0, 126,
            this.backgroundWidth, 96,
            256, 256);
}
```

This relies on `DrawContext.drawTexture(...)` overloads that let you specify UV region and the source texture size. [maven.fabricmc](https://maven.fabricmc.net/docs/yarn-1.21+build.7/net/minecraft/client/gui/DrawContext.html)

## Option B: Use other vanilla GUI PNGs

Any vanilla GUI texture can be referenced the same way (namespace `minecraft`, path under `textures/gui/...`), e.g. `textures/gui/container/anvil.png`, `textures/gui/container/furnace.png`, etc. [creativemechanicserver](https://www.creativemechanicserver.com/static/assets/minecraft/textures/gui/container/)

```java
private static final Identifier VANILLA_BG =
        new Identifier("minecraft", "textures/gui/container/furnace.png");
```

When you switch files, you must also update your UVs and your `backgroundWidth/backgroundHeight` to match how that vanilla screen is laid out (your old `(u=176, v=14)` progress slice was specific to your custom PNG). [maven.fabricmc](https://maven.fabricmc.net/docs/yarn-1.21+build.7/net/minecraft/client/gui/DrawContext.html)

## Option C: Draw it in code (no PNG)

For a fully code-drawn screen, keep slots/text as usual, but draw the panel and progress bar using `DrawContext` line/texture helpers instead of a background image. [maven.fabricmc](https://maven.fabricmc.net/docs/yarn-1.21+build.7/net/minecraft/client/gui/DrawContext.html)

Example “filled bar” using horizontal lines (no textures at all):

```java
int progress = handler.getProgress();
int max = handler.getMaxProgress();
if (max > 0) {
    int barX = x + 79, barY = y + 34;
    int barW = 24, barH = 16;

    int filled = (int)(barW * (progress / (float) max));

    // background (dark gray)
    for (int dy = 0; dy < barH; dy++) {
        context.drawHorizontalLine(barX, barX + barW - 1, barY + dy, 0xFF3A3A3A);
    }
    // fill (green)
    if (filled > 0) {
        for (int dy = 0; dy < barH; dy++) {
            context.drawHorizontalLine(barX, barX + filled - 1, barY + dy, 0xFF2ECC71);
        }
    }
}
```

`DrawContext` provides `drawHorizontalLine(...)` and multiple texture-drawing methods (`drawTexture`, `drawGuiTexture`) you can mix as needed. [maven.fabricmc](https://maven.fabricmc.net/docs/yarn-1.21+build.7/net/minecraft/client/gui/DrawContext.html)

## Quick recommendation

If you want it to look “Minecraft-native” with minimal work, start with `minecraft:textures/gui/container/generic_54.png` (or another close vanilla container) and overlay only your custom progress indicator in code. [creativemechanicserver](https://www.creativemechanicserver.com/static/assets/minecraft/textures/gui/container/)
