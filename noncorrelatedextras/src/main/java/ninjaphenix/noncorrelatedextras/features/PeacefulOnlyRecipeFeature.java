package ninjaphenix.noncorrelatedextras.features;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.noncorrelatedextras.Main;
import ninjaphenix.noncorrelatedextras.core.Feature;
import ninjaphenix.noncorrelatedextras.recipe.PeacefulOnlyShapedRecipe;
import ninjaphenix.noncorrelatedextras.recipe.PeacefulOnlyShapelessRecipe;

public class PeacefulOnlyRecipeFeature extends Feature
{
    private static PeacefulOnlyShapedRecipe createRecipe(final ShapedRecipe recipe)
    {
        return new PeacefulOnlyShapedRecipe(recipe.getId(), recipe.group, recipe.getWidth(), recipe.getHeight(), recipe.getPreviewInputs(), recipe.getOutput());
    }

    private static PeacefulOnlyShapelessRecipe createRecipe(final ShapelessRecipe recipe)
    {
        return new PeacefulOnlyShapelessRecipe(recipe.getId(), recipe.group, recipe.getOutput(), recipe.getPreviewInputs());
    }

    @Override
    public void initialise()
    {
        Registry.register(Registry.RECIPE_SERIALIZER, Main.getId("peaceful_only_crafting_shaped"), new RecipeSerializer<PeacefulOnlyShapedRecipe>()
        {
            @Override
            public PeacefulOnlyShapedRecipe read(final Identifier id, final JsonObject json) { return createRecipe(RecipeSerializer.SHAPED.read(id, json)); }

            @Override
            public PeacefulOnlyShapedRecipe read(final Identifier id, final PacketByteBuf buf) { return createRecipe(RecipeSerializer.SHAPED.read(id, buf)); }

            @Override
            public void write(final PacketByteBuf buf, final PeacefulOnlyShapedRecipe recipe) { RecipeSerializer.SHAPED.write(buf, recipe); }
        });

        Registry.register(Registry.RECIPE_SERIALIZER, Main.getId("peaceful_only_crafting_shapeless"), new RecipeSerializer<PeacefulOnlyShapelessRecipe>()
        {
            @Override
            public PeacefulOnlyShapelessRecipe read(final Identifier id, final JsonObject json) { return createRecipe(RecipeSerializer.SHAPELESS.read(id, json)); }

            @Override
            public PeacefulOnlyShapelessRecipe read(final Identifier id, final PacketByteBuf buf) { return createRecipe(RecipeSerializer.SHAPELESS.read(id, buf)); }

            @Override
            public void write(final PacketByteBuf buf, final PeacefulOnlyShapelessRecipe recipe) { RecipeSerializer.SHAPELESS.write(buf, recipe); }
        });
    }
}