package dk.mosberg.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class ABEClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.BREWING_KETTLE,
                dk.mosberg.client.screen.BrewingKettleScreen::new);
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.FERMENTATION_BARREL,
                dk.mosberg.client.screen.FermentationBarrelScreen::new);
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.DISTILLERY,
                dk.mosberg.client.screen.DistilleryScreen::new);
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.CONDENSER,
                dk.mosberg.client.screen.CondenserScreen::new);
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.AGING_BARREL,
                dk.mosberg.client.screen.AgingBarrelScreen::new);
        ScreenProviderRegistry.register(dk.mosberg.registry.ABEScreenHandlers.FLUID_BARREL,
                dk.mosberg.client.screen.FluidBarrelScreen::new);
    }
}
