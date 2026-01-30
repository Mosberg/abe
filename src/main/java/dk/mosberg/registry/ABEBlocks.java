package dk.mosberg.registry;

import dk.mosberg.ABE;
import dk.mosberg.machine.aging.AgingBarrelBlock;
import dk.mosberg.machine.condenser.CondenserBlock;
import dk.mosberg.machine.distillery.DistilleryBlock;
import dk.mosberg.machine.fermentation.FermentationBarrelBlock;
import dk.mosberg.machine.fluidbarrel.FluidBarrelBlock;
import dk.mosberg.machine.kettle.BrewingKettleBlock;
import dk.mosberg.machine.kettle_tank.BrewingKettleTankBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ABEBlocks {
    public static final Block BREWING_KETTLE = register("brewing_kettle", new BrewingKettleBlock());
    public static final Block BREWING_KETTLE_TANK =
            register("brewing_kettle_tank", new BrewingKettleTankBlock());
    public static final Block FERMENTATION_BARREL =
            register("fermentation_barrel", new FermentationBarrelBlock());
    public static final Block DISTILLERY = register("distillery", new DistilleryBlock());
    public static final Block CONDENSER = register("condenser", new CondenserBlock());
    public static final Block AGING_BARREL = register("aging_barrel", new AgingBarrelBlock());
    public static final Block FLUID_BARREL = register("fluid_barrel", new FluidBarrelBlock());

    private ABEBlocks() {}

    private static Block register(String path, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(ABE.MOD_ID, path), block);
    }

    public static void register() {
        // classload
    }
}
