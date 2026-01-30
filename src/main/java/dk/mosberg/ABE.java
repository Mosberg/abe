package dk.mosberg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEBlocks;
import dk.mosberg.registry.ABEFluidProviders;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.registry.ABEItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ABE implements ModInitializer {
    public static final String MOD_ID = "abe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Recipe types
    public static final RecipeType<?> BREWING_KETTLE_RECIPE = registerRecipeType("brewing_kettle");
    public static final RecipeType<?> FERMENTATION_BARREL_RECIPE =
            registerRecipeType("fermentation_barrel");
    public static final RecipeType<?> DISTILLERY_RECIPE = registerRecipeType("distillery");
    public static final RecipeType<?> CONDENSER_RECIPE = registerRecipeType("condenser");
    public static final RecipeType<?> AGING_BARREL_RECIPE = registerRecipeType("aging_barrel");

    private static RecipeType<?> registerRecipeType(String id) {
        return Registry.register(Registries.RECIPE_TYPE, new Identifier(MOD_ID, id),
                new RecipeType<>() {
                    public String toString() {
                        return MOD_ID + ":" + id;
                    }
                });
    }

    @Override
    public void onInitialize() {
        ABEBlocks.register();
        ABEItems.register();
        ABEFluids.register();
        ABEBlockEntities.register();
        ABEFluidProviders.register();
        dk.mosberg.registry.ABEScreenHandlers.register();
        dk.mosberg.registry.ABEItemGroups.register();

        // Register custom recipe types
        // Serializers should be registered in their respective classes if needed

        LOGGER.info("ABE initialized");
    }
}
