package dk.mosberg.registry;

import dk.mosberg.machine.condenser.CondenserBlockEntity;
import dk.mosberg.machine.distillery.DistilleryBlockEntity;
import dk.mosberg.machine.fermentation.FermentationBarrelBlockEntity;
import dk.mosberg.machine.fluidbarrel.FluidBarrelBlockEntity;
import dk.mosberg.machine.kettle.BrewingKettleBlockEntity;
import dk.mosberg.machine.kettle_tank.BrewingKettleTankBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;

public final class ABEFluidProviders {
    private ABEFluidProviders() {}

    public static void register() {
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((BrewingKettleBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.BREWING_KETTLE);
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((BrewingKettleTankBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.BREWING_KETTLE_TANK);
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((FermentationBarrelBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.FERMENTATION_BARREL);
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((DistilleryBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.DISTILLERY);
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((CondenserBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.CONDENSER);
        FluidStorage.SIDED.registerForBlockEntity(
                (be, side) -> ((FluidBarrelBlockEntity) be).getFluidStorage(side),
                ABEBlockEntities.FLUID_BARREL);
    }
}
