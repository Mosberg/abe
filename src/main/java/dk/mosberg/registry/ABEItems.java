package dk.mosberg.registry;

import dk.mosberg.ABE;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ABEItems {
    public static final Item BREWING_KETTLE =
            registerBlockItem("brewing_kettle", ABEBlocks.BREWING_KETTLE);
    public static final Item BREWING_KETTLE_TANK =
            registerBlockItem("brewing_kettle_tank", ABEBlocks.BREWING_KETTLE_TANK);
    public static final Item FERMENTATION_BARREL =
            registerBlockItem("fermentation_barrel", ABEBlocks.FERMENTATION_BARREL);
    public static final Item DISTILLERY = registerBlockItem("distillery", ABEBlocks.DISTILLERY);
    public static final Item CONDENSER = registerBlockItem("condenser", ABEBlocks.CONDENSER);
    public static final Item AGING_BARREL =
            registerBlockItem("aging_barrel", ABEBlocks.AGING_BARREL);
    public static final Item FLUID_BARREL =
            registerBlockItem("fluid_barrel", ABEBlocks.FLUID_BARREL);

    // Minimal placeholder items for your processes (you can replace later with real content).
    public static final Item YEAST = register("yeast", new Item(new FabricItemSettings()));
    public static final Item EMPTY_BOTTLE =
            register("empty_bottle", new Item(new FabricItemSettings()));
    public static final Item BOTTLE_BEER =
            register("bottle_beer", new Item(new FabricItemSettings()));
    public static final Item BOTTLE_BEER_AGED =
            register("bottle_beer_aged", new Item(new FabricItemSettings()));
    public static final Item BOTTLE_SPIRIT =
            register("bottle_spirit", new Item(new FabricItemSettings()));
    public static final Item BOTTLE_SPIRIT_AGED =
            register("bottle_spirit_aged", new Item(new FabricItemSettings()));

    private ABEItems() {}

    private static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ABE.MOD_ID, path), item);
    }

    private static Item registerBlockItem(String path, net.minecraft.block.Block block) {
        return register(path, new BlockItem(block, new FabricItemSettings()));
    }

    public static void register() {
        // classload
    }
}
