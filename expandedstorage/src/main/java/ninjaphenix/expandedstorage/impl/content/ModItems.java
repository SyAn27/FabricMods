package ninjaphenix.expandedstorage.impl.content;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.ModContent;
import ninjaphenix.expandedstorage.impl.item.ChestConversionItem;
import ninjaphenix.expandedstorage.impl.item.ChestMutatorItem;

public final class ModItems
{
    public static void init()
    {
        final Pair<Identifier, String> wood = new Pair<>(Const.id("wood_chest"), "wood");
        final Pair<Identifier, String> iron = new Pair<>(Const.id("iron_chest"), "iron");
        final Pair<Identifier, String> gold = new Pair<>(Const.id("gold_chest"), "gold");
        final Pair<Identifier, String> diamond = new Pair<>(Const.id("diamond_chest"), "diamond");
        final Pair<Identifier, String> obsidian = new Pair<>(Const.id("obsidian_chest"), "obsidian");
        final Pair<Identifier, String> netherite = new Pair<>(Const.id("netherite_chest"), "netherite");
        registerConversionPath(wood, iron, gold, diamond, obsidian, netherite);
        ChestMutatorItem chestMutator = new ChestMutatorItem();
        Registry.register(Registry.ITEM, Const.id("chest_mutator"), chestMutator);
    }

    @SafeVarargs
    private static void registerConversionPath(Pair<Identifier, String>... values)
    {
        final int length = values.length;
        for (int i = 0; i < length - 1; i++)
        {
            for (int x = i + 1; x < length; x++)
            {
                final Pair<Identifier, String> from = values[i];
                final Pair<Identifier, String> to = values[x];
                final Identifier itemId = Const.id(from.getRight() + "_to_" + to.getRight() + "_conversion_kit");
                if (!Registry.ITEM.containsId(itemId))
                {
                    Registry.register(Registry.ITEM, itemId,
                            new ChestConversionItem(new Item.Settings().group(ModContent.GROUP).maxCount(16), from.getLeft(), to.getLeft()));
                }
            }

        }

    }
}
