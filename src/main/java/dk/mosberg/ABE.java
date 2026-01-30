package dk.mosberg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEBlocks;
import dk.mosberg.registry.ABEFluidProviders;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.registry.ABEItemGroups;
import dk.mosberg.registry.ABEItems;
import dk.mosberg.registry.ABERecipeSerializers;
import dk.mosberg.registry.ABERecipes;
import dk.mosberg.registry.ABEScreenHandlers;
import net.fabricmc.api.ModInitializer;

public class ABE implements ModInitializer {
    public static final String MOD_ID = "abe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ABEBlocks.register();
        ABEItems.register();
        ABEFluids.register();
        ABEBlockEntities.register();
        ABEFluidProviders.register();
        ABEScreenHandlers.register();
        ABEItemGroups.register();

        // Register custom recipe serializers
        ABERecipeSerializers.register();

        // Register custom recipe types
        ABERecipes.register();
        // Serializers should be registered in their respective classes if needed

        LOGGER.info("ABE initialized");
    }
}
