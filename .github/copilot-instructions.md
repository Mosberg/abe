# Copilot Instructions for Alcoholic Brewery Enchanted (ABE)

## Project Overview

- **ABE** is a Minecraft Fabric 1.20.1 mod for realistic, automation-friendly brewing: wort → fermentation → (optional) distillation → aging.
- Core logic is in `src/main/java/dk/mosberg/` (machines, recipes, registry, utilities). Client code is in `src/client/java/dk/mosberg/client/`.
- Each machine (Kettle, Fermentation Barrel, Distillery, Condenser, Aging Barrel, Fluid Barrel) has its own Block, BlockEntity, Inventory, and ScreenHandler classes, grouped by function.
- Recipes, blockstates, models, and language files are in `src/main/resources/` under `assets/abe/` and `data/abe/`.

## Gradle Properties

```properties
# --------------------------------------------------------------
# Gradle Properties for The Art of Alcoholic Brewing Mod
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
  [Remote Indexing](remote-index.md)
- See `README.md` for mod description, installation, and usage.
  [README](../README.md)

## Examples

- To add a new machine: create Block, BlockEntity, Inventory, ScreenHandler, and register in `registry/`.
- To add a recipe: add JSON to `data/abe/recipes/` and update relevant machine logic.
- To add a texture/model: add to `assets/abe/textures/` or `assets/abe/models/`.

## Troubleshooting

- If a machine does not output, check orientation and required inputs (see README.md troubleshooting).
- For build errors, verify Java version and Gradle properties.

---

For more, see `README.md` and `.github/remote-index.md`.
