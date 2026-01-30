package dk.mosberg.registry;

import dk.mosberg.ABE;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ABEItemGroups {
    public static final ItemGroup MAIN =
            Registry.register(Registries.ITEM_GROUP, new Identifier(ABE.MOD_ID, "main"),
                    FabricItemGroup.builder().icon(() -> new ItemStack(ABEItems.BREWING_KETTLE))
                            .displayName(Text.translatable("itemGroup.abe.main"))
                            .entries((context, entries) -> {
                                entries.add(new ItemStack(ABEItems.BREWING_KETTLE));
                                entries.add(new ItemStack(ABEItems.BREWING_KETTLE_TANK));
                                entries.add(new ItemStack(ABEItems.FERMENTATION_BARREL));
                                entries.add(new ItemStack(ABEItems.DISTILLERY));
                                entries.add(new ItemStack(ABEItems.CONDENSER));
                                entries.add(new ItemStack(ABEItems.AGING_BARREL));
                                entries.add(new ItemStack(ABEItems.FLUID_BARREL));
                                entries.add(new ItemStack(ABEItems.YEAST));
                                entries.add(new ItemStack(ABEItems.EMPTY_BOTTLE));
                                entries.add(new ItemStack(ABEItems.BOTTLE_BEER));
                                entries.add(new ItemStack(ABEItems.BOTTLE_BEER_AGED));
                                entries.add(new ItemStack(ABEItems.BOTTLE_SPIRIT));
                                entries.add(new ItemStack(ABEItems.BOTTLE_SPIRIT_AGED));
                            }).build());

    private ABEItemGroups() {}

    public static void register() {
        // classload
    }
}
