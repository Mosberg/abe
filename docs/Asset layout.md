# Asset layout

Below is a complete, consistent starter set of **16×16** block/item textures and the matching blockstate + model JSONs for your 7 brewing blocks, using vanilla-style constraints (simple geometry, detail in texture, and top-left lighting for items).

Minecraft textures are typically authored at **16×16** for items and blocks, and block visuals are defined via blockstate JSON → model JSON → texture PNG. [blockbench](https://www.blockbench.net/wiki/guides/minecraft-style-guide/)

Put these under (replace `<modid>` everywhere):

```
src/main/resources/assets/<modid>/
  blockstates/
  models/block/
  models/item/
  textures/block/
  textures/item/
```

A “no-properties” blockstate can be a single `""` variant that always uses the same model. [blockbench](https://www.blockbench.net/wiki/guides/minecraft-style-guide/)

## Texture generator (PNG)

This script generates a cohesive “copper + steel + oak” palette and writes all required `textures/block/*.png` and `textures/item/*.png` at 16×16 (restricted ramps, no AA/brush noise).

Save as: `tools/gen_brewing_assets.py`

```py
from pathlib import Path
from PIL import Image

MODID = "<modid>"

# ---------- tiny pixel helpers ----------
def img16(fill):
    im = Image.new("RGBA", (16, 16), fill)
    return im

def px(im, x, y, c):
    if 0 <= x < 16 and 0 <= y < 16:
        im.putpixel((x, y), c)

def rect(im, x0, y0, x1, y1, c):
    for y in range(y0, y1):
        for x in range(x0, x1):
            px(im, x, y, c)

def outline(im, c):
    # 1px border
    for x in range(16):
        px(im, x, 0, c); px(im, x, 15, c)
    for y in range(16):
        px(im, 0, y, c); px(im, 15, y, c)

def shade_top_left(im, light, mid, dark):
    # Simple directional shading: top/left lighter, bottom/right darker
    for y in range(16):
        for x in range(16):
            r,g,b,a = im.getpixel((x,y))
            if a == 0:
                continue
            # weight: top-left -> +, bottom-right -> -
            w = (7 - x) + (7 - y)  # range approx [-16..14]
            if w >= 6:
                im.putpixel((x,y), (*light, a))
            elif w <= -6:
                im.putpixel((x,y), (*dark, a))
            else:
                im.putpixel((x,y), (*mid, a))

# ---------- palettes (hand-picked, vanilla-ish ramps) ----------
STEEL_L = (181, 188, 196)
STEEL_M = (132, 140, 150)
STEEL_D = (82,  88,  96)
OUTLINE = (40,  40,  44)

COPPER_L = (214, 143, 90)
COPPER_M = (176, 107, 62)
COPPER_D = (119,  67, 39)

OAK_L = (196, 164, 104)
OAK_M = (162, 129, 78)
OAK_D = (116,  87,  49)

GLASS_L = (160, 210, 220)
GLASS_M = (110, 170, 185)
GLASS_D = (70,  120, 135)

# ---------- block texture patterns ----------
def metal_panel():
    im = img16((*STEEL_M, 255))
    # plate seams
    rect(im, 0, 7, 16, 8, (*STEEL_D, 255))
    rect(im, 7, 0, 8, 16, (*STEEL_D, 255))
    # rivets
    for (x,y) in [(2,2),(13,2),(2,13),(13,13),(2,8),(13,8),(8,2),(8,13)]:
        px(im, x, y, (*STEEL_L, 255))
        px(im, x+1, y, (*STEEL_D, 255))
    shade_top_left(im, STEEL_L, STEEL_M, STEEL_D)
    outline(im, (*OUTLINE, 255))
    return im

def copper_pipe_face():
    im = img16((*STEEL_M, 255))
    # central copper pipe + elbow hint
    rect(im, 6, 2, 10, 14, (*COPPER_M, 255))
    rect(im, 9, 9, 14, 12, (*COPPER_M, 255))
    # pipe highlights/shadows
    rect(im, 6, 2, 7, 14, (*COPPER_L, 255))
    rect(im, 9, 2, 10, 14, (*COPPER_D, 255))
    rect(im, 9, 9, 14, 10, (*COPPER_L, 255))
    rect(im, 9, 11, 14, 12, (*COPPER_D, 255))
    shade_top_left(im, STEEL_L, STEEL_M, STEEL_D)
    outline(im, (*OUTLINE, 255))
    return im

def kettle_top(open_hole=True):
    im = img16((*STEEL_M, 255))
    # rim
    rect(im, 2, 2, 14, 14, (*STEEL_D, 255))
    rect(im, 3, 3, 13, 13, (*STEEL_M, 255))
    if open_hole:
        # "opening" (painted illusion)
        rect(im, 5, 5, 11, 11, (30, 30, 34, 255))
        rect(im, 5, 5, 11, 6, (*STEEL_D, 255))
        rect(im, 5, 5, 6, 11, (*STEEL_D, 255))
    shade_top_left(im, STEEL_L, STEEL_M, STEEL_D)
    outline(im, (*OUTLINE, 255))
    return im

def wooden_barrel_side(label=False, tap=False):
    im = img16((*OAK_M, 255))
    # staves
    for x in range(0, 16, 2):
        rect(im, x, 0, x+1, 16, (*OAK_D, 255))
    # bands
    rect(im, 0, 3, 16, 4, (*STEEL_D, 255))
    rect(im, 0, 12, 16, 13, (*STEEL_D, 255))
    # label
    if label:
        rect(im, 5, 6, 11, 10, (230, 220, 195, 255))
        rect(im, 5, 6, 11, 7, (*OUTLINE, 255))
        rect(im, 5, 6, 6, 10, (*OUTLINE, 255))
    # tap
    if tap:
        rect(im, 7, 10, 9, 13, (*STEEL_M, 255))
        rect(im, 6, 11, 10, 12, (*STEEL_D, 255))
    shade_top_left(im, OAK_L, OAK_M, OAK_D)
    outline(im, (*OUTLINE, 255))
    return im

def barrel_end():
    im = img16((*OAK_M, 255))
    # end grain rings
    rect(im, 2, 2, 14, 14, (*OAK_D, 255))
    rect(im, 3, 3, 13, 13, (*OAK_M, 255))
    rect(im, 5, 5, 11, 11, (*OAK_D, 255))
    rect(im, 6, 6, 10, 10, (*OAK_M, 255))
    shade_top_left(im, OAK_L, OAK_M, OAK_D)
    outline(im, (*OUTLINE, 255))
    return im

def glass_top():
    im = img16((*GLASS_M, 255))
    rect(im, 2, 2, 14, 14, (*GLASS_D, 255))
    rect(im, 3, 3, 13, 13, (*GLASS_M, 255))
    rect(im, 4, 4, 6, 6, (*GLASS_L, 255))
    shade_top_left(im, GLASS_L, GLASS_M, GLASS_D)
    outline(im, (*OUTLINE, 255))
    return im

# ---------- item icons (flat 16x16) ----------
def item_icon(kind):
    # A simple silhouette + outline + top-left lighting vibe (painted)
    im = img16((0,0,0,0))
    if kind == "kettle":
        rect(im, 3, 5, 13, 13, (*STEEL_M, 255))
        rect(im, 5, 3, 11, 5, (*STEEL_M, 255))
        rect(im, 4, 6, 12, 12, (30,30,34,255))
        rect(im, 6, 12, 10, 14, (*COPPER_M, 255))
    elif kind == "barrel":
        rect(im, 4, 3, 12, 14, (*OAK_M, 255))
        rect(im, 4, 5, 12, 6, (*STEEL_D, 255))
        rect(im, 4, 12, 12, 13, (*STEEL_D, 255))
    elif kind == "distill":
        rect(im, 5, 3, 11, 14, (*STEEL_M, 255))
        rect(im, 9, 6, 14, 9, (*COPPER_M, 255))
    elif kind == "condense":
        rect(im, 3, 6, 13, 12, (*STEEL_M, 255))
        rect(im, 4, 7, 12, 11, (*GLASS_M, 255))
        rect(im, 2, 8, 4, 10, (*COPPER_M, 255))
    else:
        rect(im, 4, 4, 12, 12, (*STEEL_M, 255))

    # hard outline (minecrafty)
    for y in range(16):
        for x in range(16):
            if im.getpixel((x,y)) [wiki.bedrock](https://wiki.bedrock.dev/guide/blockbench) == 0:
                continue
            for nx, ny in [(x-1,y),(x+1,y),(x,y-1),(x,y+1)]:
                if 0 <= nx < 16 and 0 <= ny < 16 and im.getpixel((nx,ny)) [wiki.bedrock](https://wiki.bedrock.dev/guide/blockbench) == 0:
                    px(im, x, y, (*OUTLINE, 255))
    return im

def write(im, path):
    path.parent.mkdir(parents=True, exist_ok=True)
    im.save(path)

def main():
    root = Path("src/main/resources/assets") / MODID
    tb = root / "textures/block"
    ti = root / "textures/item"

    # Brewing Kettle (Base)
    write(metal_panel(), tb/"brewing_kettle_base_side.png")
    write(copper_pipe_face(), tb/"brewing_kettle_base_front.png")
    write(kettle_top(open_hole=False), tb/"brewing_kettle_base_top.png")
    write(metal_panel(), tb/"brewing_kettle_base_bottom.png")

    # Brewing Kettle Tank (Top)
    write(metal_panel(), tb/"brewing_kettle_tank_side.png")
    write(kettle_top(open_hole=True), tb/"brewing_kettle_tank_top.png")
    write(metal_panel(), tb/"brewing_kettle_tank_bottom.png")
    write(copper_pipe_face(), tb/"brewing_kettle_tank_front.png")

    # Fermentation Barrel
    write(wooden_barrel_side(label=False), tb/"fermentation_barrel_side.png")
    write(barrel_end(), tb/"fermentation_barrel_top.png")
    write(barrel_end(), tb/"fermentation_barrel_bottom.png")

    # Distillery
    write(metal_panel(), tb/"distillery_side.png")
    write(copper_pipe_face(), tb/"distillery_front.png")
    write(kettle_top(open_hole=False), tb/"distillery_top.png")
    write(metal_panel(), tb/"distillery_bottom.png")

    # Condenser
    write(metal_panel(), tb/"condenser_side.png")
    write(glass_top(), tb/"condenser_top.png")
    write(metal_panel(), tb/"condenser_bottom.png")
    write(copper_pipe_face(), tb/"condenser_front.png")

    # Aging Barrel (bottled drinks)
    write(wooden_barrel_side(label=True), tb/"aging_barrel_side.png")
    write(barrel_end(), tb/"aging_barrel_top.png")
    write(barrel_end(), tb/"aging_barrel_bottom.png")

    # Fluid Barrel (Storage + filling station)
    write(wooden_barrel_side(label=False, tap=True), tb/"fluid_barrel_side.png")
    write(wooden_barrel_side(label=False, tap=True), tb/"fluid_barrel_front.png")
    write(barrel_end(), tb/"fluid_barrel_top.png")
    write(barrel_end(), tb/"fluid_barrel_bottom.png")

    # Item icons (optional flat)
    write(item_icon("kettle"), ti/"brewing_kettle_base.png")
    write(item_icon("kettle"), ti/"brewing_kettle_tank.png")
    write(item_icon("barrel"), ti/"fermentation_barrel.png")
    write(item_icon("distill"), ti/"distillery.png")
    write(item_icon("condense"), ti/"condenser.png")
    write(item_icon("barrel"), ti/"aging_barrel.png")
    write(item_icon("barrel"), ti/"fluid_barrel.png")

if __name__ == "__main__":
    main()
```

## Block models (models/block/\*.json)

These use vanilla parents like `minecraft:block/orientable` and `minecraft:block/cube_bottom_top`, where the parent defines the basic shape and you only supply texture slots. [blockbench](https://www.blockbench.net/wiki/guides/minecraft-style-guide/)

`models/block/brewing_kettle_base.json`

```json
{
  "parent": "minecraft:block/orientable",
  "textures": {
    "top": "<modid>:block/brewing_kettle_base_top",
    "bottom": "<modid>:block/brewing_kettle_base_bottom",
    "side": "<modid>:block/brewing_kettle_base_side",
    "front": "<modid>:block/brewing_kettle_base_front"
  }
}
```

`models/block/brewing_kettle_tank.json`

```json
{
  "parent": "minecraft:block/orientable",
  "textures": {
    "top": "<modid>:block/brewing_kettle_tank_top",
    "bottom": "<modid>:block/brewing_kettle_tank_bottom",
    "side": "<modid>:block/brewing_kettle_tank_side",
    "front": "<modid>:block/brewing_kettle_tank_front"
  }
}
```

`models/block/fermentation_barrel.json`

```json
{
  "parent": "minecraft:block/cube_bottom_top",
  "textures": {
    "side": "<modid>:block/fermentation_barrel_side",
    "top": "<modid>:block/fermentation_barrel_top",
    "bottom": "<modid>:block/fermentation_barrel_bottom"
  }
}
```

`models/block/distillery.json`

```json
{
  "parent": "minecraft:block/orientable",
  "textures": {
    "top": "<modid>:block/distillery_top",
    "bottom": "<modid>:block/distillery_bottom",
    "side": "<modid>:block/distillery_side",
    "front": "<modid>:block/distillery_front"
  }
}
```

`models/block/condenser.json`

```json
{
  "parent": "minecraft:block/orientable",
  "textures": {
    "top": "<modid>:block/condenser_top",
    "bottom": "<modid>:block/condenser_bottom",
    "side": "<modid>:block/condenser_side",
    "front": "<modid>:block/condenser_front"
  }
}
```

`models/block/aging_barrel.json`

```json
{
  "parent": "minecraft:block/cube_bottom_top",
  "textures": {
    "side": "<modid>:block/aging_barrel_side",
    "top": "<modid>:block/aging_barrel_top",
    "bottom": "<modid>:block/aging_barrel_bottom"
  }
}
```

`models/block/fluid_barrel.json`

```json
{
  "parent": "minecraft:block/orientable",
  "textures": {
    "top": "<modid>:block/fluid_barrel_top",
    "bottom": "<modid>:block/fluid_barrel_bottom",
    "side": "<modid>:block/fluid_barrel_side",
    "front": "<modid>:block/fluid_barrel_front"
  }
}
```

## Blockstates (blockstates/\*.json)

A blockstate JSON maps property variants (like `facing=`) to models and rotations, and if a block has no relevant properties you can use a single `""` variant. [blockbench](https://www.blockbench.net/wiki/guides/minecraft-style-guide/)

For the “orientable machines” (base, tank, distillery, condenser, fluid barrel), assuming your block has `HorizontalFacingBlock.FACING`:

Example: `blockstates/brewing_kettle_base.json`

```json
{
  "variants": {
    "facing=north": { "model": "<modid>:block/brewing_kettle_base", "y": 0 },
    "facing=east": { "model": "<modid>:block/brewing_kettle_base", "y": 90 },
    "facing=south": { "model": "<modid>:block/brewing_kettle_base", "y": 180 },
    "facing=west": { "model": "<modid>:block/brewing_kettle_base", "y": 270 }
  }
}
```

Repeat that same pattern for:

- `brewing_kettle_tank.json`
- `distillery.json`
- `condenser.json`
- `fluid_barrel.json`

For the two “non-facing barrels”:

`blockstates/fermentation_barrel.json`

```json
{
  "variants": {
    "": { "model": "<modid>:block/fermentation_barrel" }
  }
}
```

`blockstates/aging_barrel.json`

```json
{
  "variants": {
    "": { "model": "<modid>:block/aging_barrel" }
  }
}
```

## Item models (models/item/\*.json)

If you want the block item to render as the 3D block model, set the item model parent to your block model (this avoids needing an item PNG for that block). [blockbench](https://www.blockbench.net/wiki/guides/minecraft-style-guide/)

Example: `models/item/brewing_kettle_base.json`

```json
{
  "parent": "<modid>:block/brewing_kettle_base"
}
```

Do the same for all seven blocks (`brewing_kettle_tank`, `fermentation_barrel`, `distillery`, `condenser`, `aging_barrel`, `fluid_barrel`).

If you instead want flat icons (using the generated `textures/item/*.png`), change an item model to:

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "<modid>:item/brewing_kettle_base"
  }
}
```

What block property names are you using for these (e.g., `facing`, `lit`, `part=base/tank`), and do you want the kettle tank to visually “sit on” the base as a _separate block_ or as a single combined multiblock model?
