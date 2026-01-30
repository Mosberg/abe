package dk.mosberg.machine;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public final class MachineConstants {
    private MachineConstants() {}

    public static final long BUCKET = FluidConstants.BUCKET;

    // Tune these to your design. Minecraft tick = 20/sec.
    public static final int KETTLE_TIME_TICKS = 20 * 60 * 25; // 25 minutes
    public static final int FERMENT_TIME_TICKS = 20 * 60 * 60 * 24; // 1 day
    public static final int DISTILL_TIME_TICKS = 20 * 60 * 45; // 45 minutes
    public static final int CONDENSE_TIME_TICKS = 20 * 60 * 15; // 15 minutes
    public static final int AGE_TIME_TICKS = 20 * 60 * 60 * 24 * 7; // 7 days

    public static final long MACHINE_TANK_4B = 4 * BUCKET;
    public static final long MACHINE_TANK_16B = 16 * BUCKET;
}
