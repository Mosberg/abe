package dk.mosberg.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class DistilleryRecipeSerializer implements RecipeSerializer<Recipe<?>> {
    @Override
    public Recipe<?> read(Identifier id, JsonObject json) {
        // TODO: Implement reading from JSON
        throw new UnsupportedOperationException(
                "DistilleryRecipeSerializer.read from JSON is not implemented");
    }

    @Override
    public Recipe<?> read(Identifier id, PacketByteBuf buf) {
        // TODO: Implement reading from network
        throw new UnsupportedOperationException(
                "DistilleryRecipeSerializer.read from network is not implemented");
    }

    @Override
    public void write(PacketByteBuf buf, Recipe<?> recipe) {
        // TODO: Implement writing to network
    }
}
