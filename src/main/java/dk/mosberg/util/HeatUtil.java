package dk.mosberg.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class HeatUtil {
    private HeatUtil() {}

    public static boolean hasHeatBelow(World world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        return below.isOf(Blocks.LAVA) || below.isOf(Blocks.FIRE) || below.isOf(Blocks.CAMPFIRE)
                || below.isOf(Blocks.SOUL_CAMPFIRE);
    }
}
