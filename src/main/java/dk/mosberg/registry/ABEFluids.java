package dk.mosberg.registry;

import dk.mosberg.ABE;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.StateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public final class ABEFluids {
    public static FlowableFluid WORT_STILL;
    public static FlowableFluid WORT_FLOWING;

    public static FlowableFluid FERMENTED_STILL;
    public static FlowableFluid FERMENTED_FLOWING;

    public static FlowableFluid DISTILLATE_STILL;
    public static FlowableFluid DISTILLATE_FLOWING;

    public static FlowableFluid CONDENSED_STILL;
    public static FlowableFluid CONDENSED_FLOWING;

    private ABEFluids() {}

    public static void register() {
        WORT_STILL = register("wort", new ABEFluid.Still());
        WORT_FLOWING = register("wort_flowing", new ABEFluid.Flowing());

        FERMENTED_STILL = register("fermented", new ABEFluid.Still());
        FERMENTED_FLOWING = register("fermented_flowing", new ABEFluid.Flowing());

        DISTILLATE_STILL = register("distillate", new ABEFluid.Still());
        DISTILLATE_FLOWING = register("distillate_flowing", new ABEFluid.Flowing());

        CONDENSED_STILL = register("condensed", new ABEFluid.Still());
        CONDENSED_FLOWING = register("condensed_flowing", new ABEFluid.Flowing());
    }

    private static FlowableFluid register(String path, FlowableFluid fluid) {
        return Registry.register(Registries.FLUID, new Identifier(ABE.MOD_ID, path), fluid);
    }

    // Minimal fluid impl to satisfy 1.20.1 abstract methods.
    public abstract static class ABEFluid extends FlowableFluid {
        @Override
        public Fluid getStill() {
            return this;
        }

        @Override
        public Fluid getFlowing() {
            return this;
        }

        @Override
        public Item getBucketItem() {
            return Items.BUCKET;
        } // TODO replace with real bucket items

        @Override
        protected boolean isInfinite(World world) {
            return false;
        }

        @Override
        protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {}

        @Override
        protected int getFlowSpeed(WorldView world) {
            return 4;
        }

        @Override
        protected int getLevelDecreasePerBlock(WorldView world) {
            return 1;
        }

        @Override
        public int getTickRate(WorldView world) {
            return 5;
        }

        @Override
        protected float getBlastResistance() {
            return 100f;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos,
                Fluid fluid, Direction direction) {
            return false;
        }

        @Override
        public boolean isStill(FluidState state) {
            if (state.getProperties().contains(FlowableFluid.LEVEL)) {
                return state.get(FlowableFluid.LEVEL) == 8;
            }
            // Fallback for custom fluids that don't use LEVEL
            return true;
        }

        @Override
        public int getLevel(FluidState state) {
            if (state.getProperties().contains(FlowableFluid.LEVEL)) {
                return state.get(FlowableFluid.LEVEL);
            }
            // Fallback for custom fluids that don't use LEVEL
            return 8;
        }

        @Override
        public BlockState toBlockState(FluidState state) {
            // If you later add a FluidBlock, return that block state here.
            return net.minecraft.block.Blocks.AIR.getDefaultState();
        }

        public static class Flowing extends ABEFluid {
            @Override
            protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
                builder.add(LEVEL);
            }
        }

        public static class Still extends ABEFluid {
            // default LEVEL=8 behavior
        }
    }
}
