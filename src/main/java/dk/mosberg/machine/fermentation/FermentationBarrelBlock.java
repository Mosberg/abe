package dk.mosberg.machine.fermentation;

import dk.mosberg.machine.MachineBlockWithEntity;
import dk.mosberg.registry.ABEBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FermentationBarrelBlock extends MachineBlockWithEntity {
    @Override
    public net.minecraft.util.ActionResult onUse(net.minecraft.block.BlockState state,
            net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
            net.minecraft.entity.player.PlayerEntity player, net.minecraft.util.Hand hand,
            net.minecraft.util.hit.BlockHitResult hit) {
        if (!world.isClient) {
            var be = world.getBlockEntity(pos);
            if (be instanceof FermentationBarrelBlockEntity fermentationBe) {
                player.openHandledScreen(
                        new net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory() {
                            @Override
                            public void writeScreenOpeningData(
                                    net.minecraft.server.network.ServerPlayerEntity player,
                                    net.minecraft.network.PacketByteBuf buf) {
                                fermentationBe.writeScreenOpeningData(player, buf);
                            }

                            @Override
                            public net.minecraft.text.Text getDisplayName() {
                                return fermentationBe.getDisplayName();
                            }

                            @Override
                            public net.minecraft.screen.ScreenHandler createMenu(int syncId,
                                    net.minecraft.entity.player.PlayerInventory inv,
                                    net.minecraft.entity.player.PlayerEntity player) {
                                return new FermentationBarrelScreenHandler(syncId, inv,
                                        fermentationBe);
                            }
                        });
            }
        }
        return net.minecraft.util.ActionResult.SUCCESS;
    }

    public FermentationBarrelBlock() {
        super(Settings.create().strength(3.0f).nonOpaque());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FermentationBarrelBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null
                : checkType(type, ABEBlockEntities.FERMENTATION_BARREL,
                        FermentationBarrelBlockEntity::serverTick);
    }
}
