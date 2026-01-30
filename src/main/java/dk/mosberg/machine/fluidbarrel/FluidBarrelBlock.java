package dk.mosberg.machine.fluidbarrel;

import dk.mosberg.machine.MachineBlockWithEntity;
import dk.mosberg.registry.ABEBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidBarrelBlock extends MachineBlockWithEntity {
    public FluidBarrelBlock() {
        super(Settings.create().strength(3.0f).nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidBarrelBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null
                : checkType(type, ABEBlockEntities.FLUID_BARREL,
                        FluidBarrelBlockEntity::serverTick);
    }
}
