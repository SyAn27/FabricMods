package ninjaphenix.expandedstorage.impl.content;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.impl.Const;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.OldChestBlock;
import ninjaphenix.expandedstorage.impl.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.impl.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.impl.item.ChestConversionItem;
import ninjaphenix.expandedstorage.impl.item.ChestMutatorItem;

public final class ModContent
{
    public static final BlockEntityType<CursedChestBlockEntity> CHEST;
    public static final BlockEntityType<OldChestBlockEntity> OLD_CHEST;
    public static final CursedChestBlock WOOD_CHEST;
    public static final CursedChestBlock DIAMOND_CHEST;

    static
    {
        final ItemGroup group = ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ModItemGroup(index, Const.MOD_ID));

        WOOD_CHEST = chest(Blocks.OAK_PLANKS, "wood_chest", 3, group);
        final CursedChestBlock pumpkinChest = chest(Blocks.PUMPKIN, "pumpkin_chest", 3, group);
        final CursedChestBlock christmasChest = chest(Blocks.OAK_PLANKS, "christmas_chest", 3, group);
        final CursedChestBlock ironChest = chest(Blocks.IRON_BLOCK, "iron_chest", 6, group);
        final CursedChestBlock goldChest = chest(Blocks.GOLD_BLOCK, "gold_chest", 9, group);
        DIAMOND_CHEST = chest(Blocks.DIAMOND_BLOCK, "diamond_chest", 12, group);
        final CursedChestBlock obsidianChest = chest(Blocks.OBSIDIAN, "obsidian_chest", 12, group);
        final CursedChestBlock netheriteChest = chest(Blocks.NETHERITE_BLOCK, "netherite_chest", 15, group);
        final OldChestBlock oldWoodChest = old(Blocks.OAK_PLANKS, "wood_chest", 3, group);
        final OldChestBlock oldIronChest = old(Blocks.IRON_BLOCK, "iron_chest", 6, group);
        final OldChestBlock oldGoldChest = old(Blocks.GOLD_BLOCK, "gold_chest", 9, group);
        final OldChestBlock oldDiamondChest = old(Blocks.DIAMOND_BLOCK, "diamond_chest", 12, group);
        final OldChestBlock oldObsidianChest = old(Blocks.OBSIDIAN, "obsidian_chest", 12, group);
        final OldChestBlock oldNetheriteChest = old(Blocks.NETHERITE_BLOCK, "netherite_chest", 15, group);

        CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("cursed_chest"), BlockEntityType.Builder.create(() -> new CursedChestBlockEntity(null), WOOD_CHEST, pumpkinChest, christmasChest, ironChest, goldChest, DIAMOND_CHEST, obsidianChest, netheriteChest).build(null));
        OLD_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("old_cursed_chest"), BlockEntityType.Builder.create(() -> new OldChestBlockEntity(null), oldWoodChest, oldIronChest, oldIronChest, oldGoldChest, oldDiamondChest, oldObsidianChest, oldNetheriteChest).build(null));

        final Pair<Identifier, String> wood = new Pair<>(Const.id("wood_chest"), "wood");
        final Pair<Identifier, String> iron = new Pair<>(Const.id("iron_chest"), "iron");
        final Pair<Identifier, String> gold = new Pair<>(Const.id("gold_chest"), "gold");
        final Pair<Identifier, String> diamond = new Pair<>(Const.id("diamond_chest"), "diamond");
        final Pair<Identifier, String> obsidian = new Pair<>(Const.id("obsidian_chest"), "obsidian");
        final Pair<Identifier, String> netherite = new Pair<>(Const.id("netherite_chest"), "netherite");
        registerConversionPath(group, wood, iron, gold, diamond, obsidian, netherite);

        final ChestMutatorItem chestMutator = new ChestMutatorItem(new Item.Settings().maxCount(1).group(group));
        Registry.register(Registry.ITEM, Const.id("chest_mutator"), chestMutator);
    }

    public static void register() { }

    private static OldChestBlock old(final Block material, final String name, final int rows, final ItemGroup group)
    {
        final OldChestBlock block = new OldChestBlock(Block.Settings.copy(material));
        final Text containerName = new TranslatableText("container.expandedstorage." + name);
        final Identifier id = Const.id("old_" + name);
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.OLD_CHEST, Const.id(name), new Registries.TierData(rows * 9, containerName, id));
        return block;
    }

    private static CursedChestBlock chest(final Block material, final String name, final int rows, final ItemGroup group)
    {
        final CursedChestBlock block = new CursedChestBlock(Block.Settings.copy(material));
        final Text containerName = new TranslatableText("container.expandedstorage." + name);
        final Identifier id = Const.id(name);
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.CHEST, id, new Registries.ChestTierData(rows * 9, containerName, id, type -> Const.id(String.format("entity/%s/%s", name, type.asString()))));
        return block;
    }

    @SafeVarargs
    private static void registerConversionPath(final ItemGroup group, final Pair<Identifier, String>... values)
    {
        final int length = values.length;
        for (int i = 0; i < length - 1; i++)
        {
            for (int x = i + 1; x < length; x++)
            {
                final Pair<Identifier, String> from = values[i];
                final Pair<Identifier, String> to = values[x];
                final Identifier id = Const.id(from.getRight() + "_to_" + to.getRight() + "_conversion_kit");
                if (!Registry.ITEM.containsId(id))
                {
                    Registry.register(Registry.ITEM, id, new ChestConversionItem(new Item.Settings().group(group).maxCount(16), from.getLeft(), to.getLeft()));
                }
            }
        }
    }
}