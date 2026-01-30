package dk.mosberg.registry;

import dk.mosberg.ABE;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ABERecipes {
    public static final RecipeType<?> BREWING_KETTLE = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(ABE.MOD_ID, "brewing_kettle"), new RecipeType<>() {
                public String toString() {
                    return "abe:brewing_kettle";
                }
            });
    public static final RecipeType<?> FERMENTATION_BARREL =
            Registry.register(Registries.RECIPE_TYPE,
                    new Identifier(ABE.MOD_ID, "fermentation_barrel"), new RecipeType<>() {
                        public String toString() {
                            return "abe:fermentation_barrel";
                        }
                    });
    public static final RecipeType<?> DISTILLERY = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(ABE.MOD_ID, "distillery"), new RecipeType<>() {
                public String toString() {
                    return "abe:distillery";
                }
            });
    public static final RecipeType<?> CONDENSER = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(ABE.MOD_ID, "condenser"), new RecipeType<>() {
                public String toString() {
                    return "abe:condenser";
                }
            });
    public static final RecipeType<?> AGING_BARREL = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(ABE.MOD_ID, "aging_barrel"), new RecipeType<>() {
                public String toString() {
                    return "abe:aging_barrel";
                }
            });

    public static void register() {
        // Triggers static initializers
    }
}
