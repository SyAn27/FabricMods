package ninjaphenix.noncorrelatedextras.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class PeacefulOnlyShapedRecipe extends ShapedRecipe
{
    public PeacefulOnlyShapedRecipe(final Identifier id, final String group, final int width, final int height, final DefaultedList<Ingredient> ingredients,
            final ItemStack output)
    {
        super(id, group, width, height, ingredients, output);
    }

    @Override
    public boolean matches(final CraftingInventory craftingInventory, final World world)
    {
        return Difficulty.PEACEFUL.equals(world.getDifficulty()) && super.matches(craftingInventory, world);
    }
}