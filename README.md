# Alcoholic Brewery Enchanted

Realistic, automation-friendly brewing for **Fabric**: brew wort, ferment it, optionally distill it into spirits, and age bottled drinks.

> Tip: Exact recipes, timings, yields, and effects can be configurable—always verify in-game with JEI/REI (or your pack docs).

```properties
# --------------------------------------------------------------
# Gradle Properties for Alcoholic Brewery Enchanted Mod
# --------------------------------------------------------------

# --------------------------------------------------------------
# Gradle JVM Configuration - Optimized for Fabric Development
# --------------------------------------------------------------

org.gradle.jvmargs=-Xmx4G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true

# --------------------------------------------------------------
# Mod Metadata and Version Configuration
# --------------------------------------------------------------

maven_group=dk.mosberg
archives_base_name=abe

mod_id=abe
mod_version=1.0.0
mod_name=Alcoholic Brewery Enchanted
mod_description=Alcoholic Brewery Enchanted adds a variety of alcoholic beverages to Minecraft, enhancing the brewing experience with new recipes and effects.
mod_author=Mosberg
mod_homepage=https://mosberg.github.io/ABE
mod_sources=https://github.com/mosberg/ABE
mod_issues=https://github.com/mosberg/ABE/issues
mod_license=MIT

### Entrypoints (fully-qualified class names)
mod_entrypoint_main=dk.mosberg.ABE
mod_entrypoint_client=dk.mosberg.client.ABEClient

# --------------------------------------------------------------
# Minecraft & Fabric Versions
# --------------------------------------------------------------

minecraft_version=1.20.1
yarn_mappings=1.20.1+build.10
loader_version=0.18.4
loom_version=1.15-SNAPSHOT
fabric_version=0.92.7+1.20.1
java_version=21

# --------------------------------------------------------------
# Library Versions
# --------------------------------------------------------------

gson_version=2.13.2
slf4j_version=2.0.17
annotations_version=26.0.2

# --------------------------------------------------------------
# Testing Framework Versions
# --------------------------------------------------------------

junit_version=6.0.2

# --------------------------------------------------------------
# Suggested Dependencies Versions
# --------------------------------------------------------------

modmenu_version=7.2.2
```

---

## Quick start (first batch)

**Goal:** Build the default processing line and run a simple fermented drink.

1. Place machines in a straight line with the same facing.
2. Ensure each machine’s **South** side faces the previous machine’s output, and **North** points to the next machine.
3. Supply: water + ingredients to the kettle/tank, yeast to fermentation, heat to kettle/distillery, coolant to condenser, bottles/flasks to aging/storage.

### Default chain (South → North)

`Brewing Kettle (Base) + Brewing Kettle Tank (Top)`
→ `Fermentation Barrel`
→ `Distillery`
→ `Condenser`
→ `Aging Barrel`
→ `Fluid Barrel`

---

## Conventions

- **South = input**, **North = output** (relative to how the block is placed/facing).
- Fluids are measured in **buckets (B)**.
- Examples assume **1–4 B** per batch where applicable.
- Times are shown as “minutes/days” as configured by the mod or modpack.

---

## Machine reference

| Machine                   | Purpose                                  | Inputs (South unless noted)             | Outputs (North unless noted)                       | Notes                                                   |
| ------------------------- | ---------------------------------------- | --------------------------------------- | -------------------------------------------------- | ------------------------------------------------------- |
| Brewing Kettle (Base)     | Heat source / boiler base                | Fuel (as configured) and/or heat below  | —                                                  | Must have the Tank above it to produce wort.            |
| Brewing Kettle Tank (Top) | Holds water + ingredients; produces wort | **Bottom:** ingredients + water (1–4 B) | Wort (1–4 B)                                       | Place directly above the Kettle Base.                   |
| Fermentation Barrel       | Turns wort into fermented base           | Wort (1–4 B), yeast (1–2)               | Fermented base (1–4 B)                             | Needs yeast to start/continue.                          |
| Distillery                | Raises proof via distillation            | Fermented base (1–4 B), heat/fuel       | Distillate (often lower volume)                    | Can require multiple cycles depending on recipe/config. |
| Condenser                 | Cools distillate into a stable spirit    | Distillate, coolant item/fluid          | Condensed spirit                                   | Won’t run without coolant (per rules).                  |
| Aging Barrel              | Ages **bottled** drinks                  | Bottled drinks/flasks                   | Aged bottles/flasks                                | Wood type may affect results (if enabled).              |
| Fluid Barrel (Storage)    | Bulk storage + filling station           | Fluids in, empty flasks                 | Fluids out (all sides except South), filled flasks | No processing; great buffer at the end of a line.       |

---

## Step-by-step: fermented drink (no distillation)

1. **Kettle/Tank:** Insert water + your recipe ingredients into the Tank (Tank bottom input); provide heat/fuel to the Kettle Base.
2. Wait for **wort** to output to the north.
3. **Fermentation Barrel:** Feed wort in + add yeast; wait for fermented output.
4. (Optional) Send fermented output into **Fluid Barrel** for storage/filling, or bottle it and move it to **Aging Barrel**.

---

## Step-by-step: spirit (distillation chain)

1. Produce **fermented base** (as above).
2. **Distillery:** Provide heat/fuel; send fermented base in; collect distillate out.
3. **Condenser:** Provide coolant; send distillate in; collect condensed spirit out.
4. Bottle and optionally **age** in the Aging Barrel.

---

## Example recipes (starter-friendly)

These are **example patterns** using mostly vanilla ingredients plus core mod items (like yeast). Treat quantities/yields as pack-dependent.

### 1) Apple cider (fermented)

- Kettle/Tank: apples + water
- Fermentation: add yeast
- Optional: age bottled cider for smoother results

### 2) Wheat beer (fermented)

- Kettle/Tank: wheat + water (+ “bittering” ingredient if your pack uses one)
- Fermentation: add yeast
- Optional: age for flavor/quality

### 3) Berry wine (fermented)

- Kettle/Tank: sweet berries (or glow berries) + water
- Fermentation: add yeast
- Optional: age bottled wine

### 4) Pumpkin ale (fermented)

- Kettle/Tank: pumpkin + wheat + water (+ optional bittering/flavoring)
- Fermentation: add yeast
- Optional: age bottled ale

### 5) Potato spirit (distilled)

- Kettle/Tank: potatoes + water
- Fermentation: add yeast
- Distill + condense
- Optional: age bottled spirit

---

## Vanilla ingredient ideas (by category)

Use these as inspiration when browsing/authoring recipes:

- Grains/starches: wheat, potatoes, beetroot, carrots
- Fruits: apples, melon slices, sweet berries, glow berries, pumpkin
- Aromatics (pack-dependent): flowers (dandelion, cornflower, etc.)
- Sugars: sugar cane (as sugar), honey-related items (if supported)
- “Weird” brews: nether wart, cactus, kelp, chorus fruit (only if recipes exist)

---

## Automation tips

- Keep the whole line aligned and facing consistently; most “it doesn’t output” issues are orientation.
- Use a buffer (Fluid Barrel) between stages if upstream machines produce in bursts.
- If a stage stalls: check **heat/fuel**, **coolant**, **yeast**, and available output space first.

---

## Troubleshooting

- **Kettle runs but no wort appears:** Tank not placed above Base, missing water, or output side blocked.
- **Fermenter won’t start:** missing yeast or wrong input fluid.
- **Distillery outputs but condenser does nothing:** condenser missing coolant or mis-oriented.
- **Aging doesn’t work:** requires bottled items (not fluids) and output space.

---

## Contributing / Issues

- Found a bug or balance issue? Open an issue with: mod version, Minecraft version, loader/API versions, and steps to reproduce.
- PRs welcome—keep changes focused and document new recipes/config options.

## License

MIT License. See `LICENSE` file for details.
