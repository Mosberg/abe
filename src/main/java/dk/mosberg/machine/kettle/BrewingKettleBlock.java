package dk.mosberg.machine.kettle;

import dk.mosberg.machine.MachineBlockWithEntity;
import dk.mosberg.registry.ABEBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrewingKettleBlock extends MachineBlockWithEntity {
    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state,
            net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BrewingKettleBlockEntity kettleBe) {
                player.openHandledScreen(
                        new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory() {
                            @Override
                            public void writeScreenOpeningData(
                                    net.minecraft.server.network.ServerPlayerEntity player,
                                    net.minecraft.network.PacketByteBuf buf) {
                                // If you need to sync extra data, write it here (e.g.,
                                // buf.writeBlockPos(pos))
                                buf.writeBlockPos(pos);
                            }

                            @Override
                            public net.minecraft.text.Text getDisplayName() {
                                return kettleBe.getDisplayName();
                            }

                            @Override
                            public net.minecraft.screen.ScreenHandler createMenu(int syncId,
                                    net.minecraft.entity.player.PlayerInventory inv,
                                    net.minecraft.entity.player.PlayerEntity player) {
                                return new dk.mosberg.machine.kettle.BrewingKettleScreenHandler(
                                        syncId, inv, kettleBe.inv);
                            }
                        });
            }
        }
        return net.minecraft.util.ActionResult.SUCCESS;
    }

    public BrewingKettleBlock() {
        super(Settings.create().strength(3.0f).nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BrewingKettleBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null
                : checkType(type, ABEBlockEntities.BREWING_KETTLE,
                        BrewingKettleBlockEntity::serverTick);
    }
}
