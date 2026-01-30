package dk.mosberg.machine.condenser;

import dk.mosberg.machine.MachineBlockWithEntity;
import dk.mosberg.registry.ABEBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CondenserBlock extends MachineBlockWithEntity {
    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state,
            net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand,
            net.minecraft.util.hit.BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory) {
                player.openHandledScreen(
                        (net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory) be);
            }
        }
        return net.minecraft.util.ActionResult.SUCCESS;
    }

    public CondenserBlock() {
        super(Settings.create().strength(3.0f).nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CondenserBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null
                : checkType(type, ABEBlockEntities.CONDENSER, CondenserBlockEntity::serverTick);
    }
}
