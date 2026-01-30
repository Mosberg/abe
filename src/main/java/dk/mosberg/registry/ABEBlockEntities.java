package dk.mosberg.registry;

import dk.mosberg.ABE;
import dk.mosberg.machine.aging.AgingBarrelBlockEntity;
import dk.mosberg.machine.condenser.CondenserBlockEntity;
import dk.mosberg.machine.distillery.DistilleryBlockEntity;
import dk.mosberg.machine.fermentation.FermentationBarrelBlockEntity;
import dk.mosberg.machine.fluidbarrel.FluidBarrelBlockEntity;
import dk.mosberg.machine.kettle.BrewingKettleBlockEntity;
import dk.mosberg.machine.kettle_tank.BrewingKettleTankBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ABEBlockEntities {
    public static final BlockEntityType<BrewingKettleBlockEntity> BREWING_KETTLE = Registry
            .register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ABE.MOD_ID, "brewing_kettle"),
                    BlockEntityType.Builder
                            .create(BrewingKettleBlockEntity::new, ABEBlocks.BREWING_KETTLE)
                            .build(null));

    public static final BlockEntityType<BrewingKettleTankBlockEntity> BREWING_KETTLE_TANK =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(ABE.MOD_ID, "brewing_kettle_tank"),
                    BlockEntityType.Builder.create(BrewingKettleTankBlockEntity::new,
                            ABEBlocks.BREWING_KETTLE_TANK).build(null));

    public static final BlockEntityType<FermentationBarrelBlockEntity> FERMENTATION_BARREL =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(ABE.MOD_ID, "fermentation_barrel"),
                    BlockEntityType.Builder.create(FermentationBarrelBlockEntity::new,
                            ABEBlocks.FERMENTATION_BARREL).build(null));

    public static final BlockEntityType<DistilleryBlockEntity> DISTILLERY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(ABE.MOD_ID, "distillery"), BlockEntityType.Builder
                            .create(DistilleryBlockEntity::new, ABEBlocks.DISTILLERY).build(null));

    public static final BlockEntityType<CondenserBlockEntity> CONDENSER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ABE.MOD_ID, "condenser"),
                    BlockEntityType.Builder.create(CondenserBlockEntity::new, ABEBlocks.CONDENSER)
                            .build(null));

    public static final BlockEntityType<AgingBarrelBlockEntity> AGING_BARREL = Registry
            .register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ABE.MOD_ID, "aging_barrel"),
                    BlockEntityType.Builder
                            .create(AgingBarrelBlockEntity::new, ABEBlocks.AGING_BARREL)
                            .build(null));

    public static final BlockEntityType<FluidBarrelBlockEntity> FLUID_BARREL = Registry
            .register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ABE.MOD_ID, "fluid_barrel"),
                    BlockEntityType.Builder
                            .create(FluidBarrelBlockEntity::new, ABEBlocks.FLUID_BARREL)
                            .build(null));

    private ABEBlockEntities() {}

    public static void register() {
        // classload
    }
}
