package ninjaphenix.expandedstorage.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.entity.OldChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OldChestBlock;
import ninjaphenix.expandedstorage.common.item.ChestConversionItem;
import ninjaphenix.expandedstorage.common.item.ChestMutatorItem;

public final class ModContent
{
    public static final BlockEntityType<CursedChestBlockEntity> CHEST;
    public static final BlockEntityType<OldChestBlockEntity> OLD_CHEST;
    public static final CursedChestBlock DIAMOND_CHEST;

    static
    {
        final ItemGroup group = ChainmailCommonApi.INSTANCE.registerItemGroup((index) -> new ItemGroup(index, Const.MOD_ID)
        {
            @Override
            @Environment(EnvType.CLIENT)
            public ItemStack createIcon() { return new ItemStack(DIAMOND_CHEST); }
        });
        CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("cursed_chest"),
                                  BlockEntityType.Builder.create(() -> new CursedChestBlockEntity(null),
                                                                 chest(Blocks.OAK_PLANKS, "wood_chest", 3, group),
                                                                 chest(Blocks.PUMPKIN, "pumpkin_chest", 3, group),
                                                                 chest(Blocks.OAK_PLANKS, "christmas_chest", 3, group),
                                                                 chest(Blocks.IRON_BLOCK, "iron_chest", 6, group),
                                                                 chest(Blocks.GOLD_BLOCK, "gold_chest", 9, group),
                                                                 DIAMOND_CHEST = chest(Blocks.DIAMOND_BLOCK, "diamond_chest", 12, group),
                                                                 chest(Blocks.OBSIDIAN, "obsidian_chest", 12, group),
                                                                 chest(Blocks.NETHERITE_BLOCK, "netherite_chest", 15, group)).build(null));
        OLD_CHEST = Registry.register(Registry.BLOCK_ENTITY_TYPE, Const.id("old_cursed_chest"),
                                      BlockEntityType.Builder.create(() -> new OldChestBlockEntity(null),
                                                                     old(Blocks.OAK_PLANKS, "wood_chest", 3, group),
                                                                     old(Blocks.IRON_BLOCK, "iron_chest", 6, group),
                                                                     old(Blocks.GOLD_BLOCK, "gold_chest", 9, group),
                                                                     old(Blocks.DIAMOND_BLOCK, "diamond_chest", 12, group),
                                                                     old(Blocks.OBSIDIAN, "obsidian_chest", 12, group),
                                                                     old(Blocks.NETHERITE_BLOCK, "netherite_chest", 15, group)).build(null));
        registerConversionPath(group,
                               new Pair<>(Const.id("wood_chest"), "wood"),
                               new Pair<>(Const.id("iron_chest"), "iron"),
                               new Pair<>(Const.id("gold_chest"), "gold"),
                               new Pair<>(Const.id("diamond_chest"), "diamond"),
                               new Pair<>(Const.id("obsidian_chest"), "obsidian"),
                               new Pair<>(Const.id("netherite_chest"), "netherite"));
        Registry.register(Registry.ITEM, Const.id("chest_mutator"), new ChestMutatorItem(new Item.Settings().maxCount(1).group(group)));
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() { }

    private static OldChestBlock old(final Block material, final String name, final int rows, final ItemGroup group)
    {
        final OldChestBlock block = new OldChestBlock(Block.Settings.copy(material));
        final Identifier id = Const.id("old_" + name);
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.OLD_CHEST, Const.id(name), new Registries.TierData(
                rows * 9, new TranslatableText("container.expandedstorage." + name), id));
        return block;
    }

    private static CursedChestBlock chest(final Block material, final String name, final int rows, final ItemGroup group)
    {
        final CursedChestBlock block = new CursedChestBlock(Block.Settings.copy(material));
        final Identifier id = Const.id(name);
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(group)));
        Registry.register(Registries.CHEST, id, new Registries.ChestTierData(
                rows * 9, new TranslatableText("container.expandedstorage." + name), id,
                type -> Const.id(String.format("entity/%s/%s", name, type.asString()))));
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
                Registry.register(Registry.ITEM, id, new ChestConversionItem(new Item.Settings().group(group).maxCount(16), from, to));
            }
        }
    }
}