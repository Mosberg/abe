# Copilot Instructions for Alcoholic Brewery Enchanted (ABE)

## Project Overview

- **ABE** is a Minecraft Fabric 1.20.1 mod for realistic, automation-friendly brewing: wort → fermentation → (optional) distillation → aging.
- Core logic is in `src/main/java/dk/mosberg/` (machines, recipes, registry, utilities). Client code is in `src/client/java/dk/mosberg/client/`.
- Each machine (Kettle, Fermentation Barrel, Distillery, Condenser, Aging Barrel, Fluid Barrel) has its own Block, BlockEntity, Inventory, and ScreenHandler classes, grouped by function.
- Recipes, blockstates, models, and language files are in `src/main/resources/` under `assets/abe/` and `data/abe/`.

## Build & Run

- **Build:** `./gradlew build` (outputs to `build/libs/`)
- **Run Client:** `./gradlew runClient` (uses `run/` for MC instance)
- **Run Server:** `./gradlew runServer` (uses `run-server/`)
- **Test:** `./gradlew test` (JUnit 6, see `build.gradle`)
- Java 21 required (see `gradle.properties`).

## Key Conventions & Patterns

- **Block orientation:** South = input, North = output. All machines follow this for automation.
- **Fluid units:** Always measured in buckets (B).
- **Entrypoints:** Main: `dk.mosberg.ABE`, Client: `dk.mosberg.client.ABEClient` (see `gradle.properties`).
- **Registry:** All blocks, items, fluids, and screen handlers are registered in `src/main/java/dk/mosberg/registry/`.
- **Machine logic:** Each machine type has a dedicated package (e.g., `machine/aging/`, `machine/distillery/`).
- **Resource expansion:** `fabric.mod.json` is templated via Gradle; update properties in `gradle.properties`.
- **Testing:** Uses JUnit Jupiter (see `build.gradle`).

## External Integrations

- **Fabric API** and **Yarn mappings** are managed via Gradle (see `build.gradle`).
- **ModMenu** is a suggested dependency (not bundled).
- See `.github/remote-index.md` for links to upstream docs and APIs.

## Examples

- To add a new machine: create Block, BlockEntity, Inventory, ScreenHandler, and register in `registry/`.
- To add a recipe: add JSON to `data/abe/recipes/` and update relevant machine logic.
- To add a texture/model: add to `assets/abe/textures/` or `assets/abe/models/`.

## Troubleshooting

- If a machine does not output, check orientation and required inputs (see README.md troubleshooting).
- For build errors, verify Java version and Gradle properties.

---

For more, see `README.md` and `.github/remote-index.md`.
