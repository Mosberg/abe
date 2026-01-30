package dk.mosberg.registry;

import dk.mosberg.ABE;
import dk.mosberg.recipe.AgingBarrelRecipeSerializer;
import dk.mosberg.recipe.BrewingKettleRecipeSerializer;
import dk.mosberg.recipe.CondenserRecipeSerializer;
import dk.mosberg.recipe.DistilleryRecipeSerializer;
import dk.mosberg.recipe.FermentationBarrelRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ABERecipeSerializers {
    public static final RecipeSerializer<?> BREWING_KETTLE = Registry.register(
            Registries.RECIPE_SERIALIZER, new Identifier(ABE.MOD_ID, "brewing_kettle"),
            new BrewingKettleRecipeSerializer());
    public static final RecipeSerializer<?> FERMENTATION_BARREL = Registry.register(
            Registries.RECIPE_SERIALIZER, new Identifier(ABE.MOD_ID, "fermentation_barrel"),
            new FermentationBarrelRecipeSerializer());
    public static final RecipeSerializer<?> DISTILLERY =
            Registry.register(Registries.RECIPE_SERIALIZER,
                    new Identifier(ABE.MOD_ID, "distillery"), new DistilleryRecipeSerializer());
    public static final RecipeSerializer<?> CONDENSER =
            Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(ABE.MOD_ID, "condenser"),
                    new CondenserRecipeSerializer());
    public static final RecipeSerializer<?> AGING_BARREL =
            Registry.register(Registries.RECIPE_SERIALIZER,
                    new Identifier(ABE.MOD_ID, "aging_barrel"), new AgingBarrelRecipeSerializer());

    public static void register() {
        // Just loads the class to trigger static initializers
    }
}
