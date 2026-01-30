package dk.mosberg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.registry.ABEBlockEntities;
import dk.mosberg.registry.ABEBlocks;
import dk.mosberg.registry.ABEFluidProviders;
import dk.mosberg.registry.ABEFluids;
import dk.mosberg.registry.ABEItems;
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
        dk.mosberg.registry.ABEScreenHandlers.register();

        LOGGER.info("ABE initialized");
    }
}
