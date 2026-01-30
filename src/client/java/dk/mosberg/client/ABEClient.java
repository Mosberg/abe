package dk.mosberg.client;

import dk.mosberg.client.screen.AgingBarrelScreen;
import dk.mosberg.client.screen.BrewingKettleScreen;
import dk.mosberg.client.screen.CondenserScreen;
import dk.mosberg.client.screen.DistilleryScreen;
import dk.mosberg.client.screen.FermentationBarrelScreen;
import dk.mosberg.client.screen.FluidBarrelScreen;
import dk.mosberg.registry.ABEScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ABEClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerScreens();
    }

    private void registerScreens() {
        HandledScreens.register(ABEScreenHandlers.BREWING_KETTLE, BrewingKettleScreen::new);
        HandledScreens.register(ABEScreenHandlers.FERMENTATION_BARREL,
                FermentationBarrelScreen::new);
        HandledScreens.register(ABEScreenHandlers.DISTILLERY, DistilleryScreen::new);
        HandledScreens.register(ABEScreenHandlers.CONDENSER, CondenserScreen::new);
        HandledScreens.register(ABEScreenHandlers.AGING_BARREL, AgingBarrelScreen::new);
        HandledScreens.register(ABEScreenHandlers.FLUID_BARREL, FluidBarrelScreen::new);
    }
}
