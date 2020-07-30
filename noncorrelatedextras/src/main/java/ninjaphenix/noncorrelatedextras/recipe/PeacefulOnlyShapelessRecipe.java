package ninjaphenix.noncorrelatedextras.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class PeacefulOnlyShapelessRecipe extends ShapelessRecipe
{
    public PeacefulOnlyShapelessRecipe(final Identifier id, final String group, final ItemStack output, final DefaultedList<Ingredient> input)
    {
        super(id, group, output, input);
    }

    @Override
    public boolean matches(final CraftingInventory craftingInventory, final World world)
    {
        return Difficulty.PEACEFUL.equals(world.getDifficulty()) && super.matches(craftingInventory, world);
    }
}