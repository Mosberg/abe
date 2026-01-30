package dk.mosberg.registry;

import dk.mosberg.machine.aging.AgingBarrelScreenHandler;
import dk.mosberg.machine.condenser.CondenserScreenHandler;
import dk.mosberg.machine.distillery.DistilleryScreenHandler;
import dk.mosberg.machine.fermentation.FermentationBarrelScreenHandler;
import dk.mosberg.machine.fluidbarrel.FluidBarrelScreenHandler;
import dk.mosberg.machine.kettle.BrewingKettleScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ABEScreenHandlers {
    public static final ExtendedScreenHandlerType<BrewingKettleScreenHandler> BREWING_KETTLE =
            BrewingKettleScreenHandler.TYPE;
    public static final ExtendedScreenHandlerType<FermentationBarrelScreenHandler> FERMENTATION_BARREL =
            FermentationBarrelScreenHandler.TYPE;
    public static final ExtendedScreenHandlerType<DistilleryScreenHandler> DISTILLERY =
            DistilleryScreenHandler.TYPE;
    public static final ExtendedScreenHandlerType<CondenserScreenHandler> CONDENSER =
            CondenserScreenHandler.TYPE;
    public static final ExtendedScreenHandlerType<AgingBarrelScreenHandler> AGING_BARREL =
            AgingBarrelScreenHandler.TYPE;
    public static final ExtendedScreenHandlerType<FluidBarrelScreenHandler> FLUID_BARREL =
            FluidBarrelScreenHandler.TYPE;

    private ABEScreenHandlers() {}

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "brewing_kettle"),
                BREWING_KETTLE);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "fermentation_barrel"),
                FERMENTATION_BARREL);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "distillery"),
                DISTILLERY);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "condenser"), CONDENSER);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "aging_barrel"),
                AGING_BARREL);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("abe", "fluid_barrel"),
                FLUID_BARREL);
    }
}
